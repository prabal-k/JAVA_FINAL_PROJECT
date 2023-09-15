import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Car_shape extends JPanel implements KeyListener, ActionListener,Runnable {
    JFrame frame = new JFrame();
    private String playerName = "";
    int fps = 60;
    private boolean gameOver = false; // Flag to check if the game is over
    private boolean dataAppended = false;
    Thread gameThread;
    boolean carf = false, carb = false, carup = false, space = false, bomb = false;
    int framewidth = 1200, frameheight = 700;
    //for background image
    ImageIcon backgroundimg = new ImageIcon("src\\background.jpg");
    private BufferedImage offscreenImage;
    private int imageX = 0;
    ImageIcon carimage = new ImageIcon("src\\car.png");
    ImageIcon flyingenemyimage = new ImageIcon("src\\flyingenemy1.gif");
    ImageIcon groundenemyimage = new ImageIcon("src\\groundenemy.gif");
    ImageIcon bulletimage = new ImageIcon("src\\bullet.gif");
    ImageIcon enemybulletimage = new ImageIcon("src\\enemybullet.png");
    ImageIcon bullethealthimage = new ImageIcon("src\\bullethealth.gif");
    ImageIcon forwardimage = new ImageIcon("src\\still.gif");
    ImageIcon backwardimage = new ImageIcon("src\\rstill.gif");
    ImageIcon explosionimage = new ImageIcon("src\\explosion.gif");
    ImageIcon bombimage = new ImageIcon("src\\bomb.gif");
    ImageIcon fly = new ImageIcon("src\\fly.gif");
    private File audioFile = new File("src\\explosion.wav");
    int xpos = 300, ypos = 530;
    Music obj = new Music();
    private Timer timer;
    private int carSpeedY = 0;
    private double gravity = 1;
    private boolean isJumping = false, gamerunning = true;
    //for score
    private int elapsedTime = 0,bombcount=5,bulletSpeed = 10, bulletDistance = 4000;
    private int bulletX = xpos + 80;
    private int bulletY = ypos + 20;
    int flyingenemyx = 2500, flyingenemyy = 200, groundenemyx = 2000, groundenemyy = 445;
    private ArrayList<Rectangle> groundenemy = new ArrayList<>();
    private ArrayList<Rectangle> flyingenemy2 = new ArrayList<>();
    private ArrayList<Integer> groundenemyHitCount = new ArrayList<>();
    private ArrayList<Rectangle> flyingenemy = new ArrayList<>();
    private ArrayList<Integer> flyingenemyHitCount = new ArrayList<>();
    int enemydeathcount = 0,highestKilled = 0,bombxpos, bombypos;
    String time = "", username = "";
    boolean bombdown=false,explosion = false, isSpaceKeyPressed = false, enterPressed = false,gameRunning = true;
    private long explosionStartTime = 0;
    private int explosionX, explosionY;
    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private ArrayList<Rectangle> bullethealth = new ArrayList<>();
    private int bulletCount = 50;
    private int lastEnemySpawnTime = 0;
    private int enemySpawnInterval = 1000;
    private int enemyHitCountIncrement = 1;
    private ArrayList<FlyingEnemyBullet> flyingEnemyBullets = new ArrayList<>();
    private int bulletFireInterval = 200;
    private long lastBulletFireTime = 0;

    //constructor for initialization
    public Car_shape() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        Timer htimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(carf)
                {
                    elapsedTime++;           //to measure distance travelled
                }
                if(carb)
                {
                    elapsedTime--;
                }
            }
        });
        htimer.start();
        Timer timer1 = new Timer(16, this);
        timer1.start();
        playerName = showCustomInputDialog();

        //to make the count of ground and flying enemy infinite
        Timer enemySpawnTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnGroundEnemy();
                spawnFlyingEnemy();
            }
        });
        enemySpawnTimer.start();

        //for bullethealth
        bullethealth.add(new Rectangle(14000,200, bullethealthimage.getIconWidth(), bullethealthimage.getIconHeight()));
        bullethealth.add(new Rectangle(29000,300, bullethealthimage.getIconWidth(), bullethealthimage.getIconHeight()));
        bullethealth.add(new Rectangle(36000,100, bullethealthimage.getIconWidth(), bullethealthimage.getIconHeight()));

        for (int i = 0; i < groundenemy.size(); i++) {
            groundenemyHitCount.add(0);
        }

        for (int i = 0; i < flyingenemy.size(); i++) {
            flyingenemyHitCount.add(0);
        }
    }

    //to determine the random position to draw ground enemies
    private void spawnGroundEnemy() {
        Random random = new Random();
        int initialX = groundenemyx + framewidth + random.nextInt(500);
        groundenemy.add(new Rectangle(initialX, groundenemyy, groundenemyimage.getIconWidth(), groundenemyimage.getIconHeight()));
        groundenemyHitCount.add(0);
    }
    //to determine the random position to draw flying enemies
    private void spawnFlyingEnemy() {
        Random random = new Random();
        int initialX = flyingenemyx + framewidth + random.nextInt(500);
        int initialY = random.nextInt(frameheight - 300);
        flyingenemy.add(new Rectangle(initialX, initialY, flyingenemyimage.getIconWidth(), flyingenemyimage.getIconHeight()));
        flyingenemyHitCount.add(0);
    }

    //to take the username as input from the player
    private String showCustomInputDialog() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Enter your name:");
        JTextField inputField = new JTextField(20);

        inputField.setFont(new Font("Ink free", Font.BOLD, 25));
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        inputField.setPreferredSize(new Dimension(200, 40));

        inputPanel.add(label);
        inputPanel.add(inputField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Enter Player Name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return inputField.getText();
        } else {
            return ""; // Return an empty string if canceled
        }
    }

    //to setup the frame
    void shapeed() {
        frame.setSize(framewidth, frameheight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
        //for game control loop (fps)//to start the game thread
        gameThread = new Thread(this);
        gameThread.start();
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);
        frame.setVisible(true);
        ImageIcon cursorImage = new ImageIcon("src/cursor.png");
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage.getImage(),
                new Point(0, 0),
                "customCursor"
        );
        frame.setCursor(customCursor);

    }

    //play sound when an enemy dies (boom)
    private void playExplosionSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //to draw all the graphical components on the screen (all images realted stuff)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (offscreenImage == null) {
            offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2d = offscreenImage.createGraphics();
        int imageWidth = backgroundimg.getIconWidth();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the background image
        g2d.drawImage(backgroundimg.getImage(), -imageX, 0, imageWidth, getHeight(), this);
        g2d.drawImage(backgroundimg.getImage(), imageWidth - imageX, 0, imageWidth, getHeight(), this);

        // Draw score in mm:ss format
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Ink free", Font.BOLD, 30));
        g2d.drawString("Distance: " + elapsedTime+" m", 10, 30);
        g2d.setFont(new Font("Ink free", Font.BOLD, 30));
        g2d.drawString("Bullets: " + bulletCount, 10, 60); // Display the bullet count

        if(bombcount>=1)
        {
            g2d.drawString(String.valueOf(bombcount), 740, 28);
        }
        else if(bombcount<=0)
        {
            g2d.drawString("0", 740, 28);

        }
        g2d.setColor(Color.BLACK);
        g2d.drawString("Killed: " + enemydeathcount, 500, 30);

        //for flying enemy
        Iterator <Rectangle> flyingenemyiterator =flyingenemy.iterator();
        while(flyingenemyiterator.hasNext())
        {
            Rectangle flyingenemyimage1 = flyingenemyiterator.next();
            if (flyingenemyimage1 != null) {
                if (carf) {
                    flyingenemyimage1.x -= 3;
                }
                else
                {
                    flyingenemyimage1.x -= 1;
                }

                g2d.drawImage(flyingenemyimage.getImage(), flyingenemyimage1.x, flyingenemyimage1.y, this);
            }


        }

        //for ground enemy
        Iterator <Rectangle> groundenemyiterator =groundenemy.iterator();
        while(groundenemyiterator.hasNext()) {
            Rectangle groundenemyimage1 = groundenemyiterator.next();
            if (groundenemyimage1 != null) {
                if (carf) {
                    groundenemyimage1.x -= 3;
                } else {
                    groundenemyimage1.x -= 1;
                }
                g2d.drawImage(groundenemyimage.getImage(), groundenemyimage1.x, groundenemyimage1.y, this);
            }
        }

        //for bullet count increase
        Iterator<Rectangle> bulletIterator = bullethealth.iterator();
        while (bulletIterator.hasNext()) {
            Rectangle bulletimage1 = bulletIterator.next();

            if (bulletimage1 != null) {
                if (carf) {
                    bulletimage1.x -= 2;
                }

                g2d.drawImage(bullethealthimage.getImage(), bulletimage1.x, bulletimage1.y, this);

                Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());
                if (carrect.intersects(bulletimage1)) {
                    bulletCount += 5;
                    bulletIterator.remove(); // Remove the bullethealthimage safely
                }
            }
        }



        if (carf) {
            g2d.drawImage(forwardimage.getImage(), xpos, ypos, null);
        } else if (carb) {
            g2d.drawImage(backwardimage.getImage(), xpos, ypos, null);
        } else if (carup) {
            g2d.drawImage(carimage.getImage(), xpos, ypos, null);
            g2d.drawImage(fly.getImage(), xpos + 38, ypos + 34, null);
        } else {
            g2d.drawImage(carimage.getImage(), xpos, ypos, null);
        }
        if (space) {
            g2d.drawImage(bulletimage.getImage(), bulletX, bulletY, this);
        }
        if (carup && carf) {
            g2d.drawImage(fly.getImage(), xpos + 35, ypos + 30, null);
        }
        if (bomb) {
            g2d.drawImage(bombimage.getImage(), bombxpos + 20, bombypos + 40, null);
        }


        if (!gamerunning) {
            try {
                gameover(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (explosion && System.currentTimeMillis() - explosionStartTime <= 2000) {
            if(carf) {
                explosionX-=3;
                g2d.drawImage(explosionimage.getImage(), explosionX, explosionY, this);
            }
            else {
                g2d.drawImage(explosionimage.getImage(), explosionX, explosionY, this);
            }
        }
        else
        {
            explosionStartTime=0;
        }
        for (Rectangle bullet : bullets) {
            g2d.drawImage(bulletimage.getImage(), bullet.x, bullet.y, this);
        }
        // Draw enemy-fired bullets
        for (FlyingEnemyBullet bullet : flyingEnemyBullets)
        {
            g2d.drawImage(enemybulletimage.getImage(), bullet.getX(), bullet.getY(), this);
        }

        g2d.drawImage(bombimage.getImage(),700,0,null);

        g2d.dispose(); // Release resources
        g.drawImage(offscreenImage, 0, 0, this);
    }
    //when game is over this method is called to store name, enemieskilled and distance to
    //the file and also read the highest score from the file and display leaderboard

    private void gameover(Graphics g) throws IOException {
        if (!dataAppended) {
            File file = new File("Score.txt");

            //TO WRITE IN FILE
            FileWriter filewriter = new FileWriter(file, true);
            filewriter.write("Killed: " + enemydeathcount + " ");
            filewriter.write(elapsedTime + " ");

            if(playerName==null||playerName.isEmpty()) {
                filewriter.write("null" + "\n");

            }
            else {
                filewriter.write(playerName + "\n");
            }
            filewriter.close();
            dataAppended = true;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("Score.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    int killed = Integer.parseInt(parts[1]);
                    if (killed > highestKilled) {
                        highestKilled = killed;
                        time = parts[2];
                        username = parts[3];
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        gameOver = true;
        carf = false;
        carb = false;
        carup = false;
        space = false;
        sound(false);
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 100));
        g.drawString("Game Over", 420, 100);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("You Killed: " + enemydeathcount, 500, 200);
        g.drawString("Distance " + elapsedTime+" m", 500, 250);
        g.drawString("Best score: " , 500, 400);
        g.drawString("Playername: "+username, 500, 450);
        g.drawString("Killed: " + String.valueOf(highestKilled), 500, 500);
        g.drawString("Distance: "+time +" m", 500, 550);
        g.setColor(Color.RED);
        g.drawString("Press Enter to Restart", 450, 300);
        if (enterPressed && !gamerunning) {
            restartGame();
        }
        
    }

    //for game control loop (fps)
    @Override
    public void run() {
        double drawinterval = 1000000000 / fps;
        double delta = 0;
        long lasttime = System.nanoTime();
        long currenttime;

        while (gameThread != null) {

            currenttime = System.nanoTime();
            delta += (currenttime - lasttime) / drawinterval;
            lasttime = currenttime;
            if (delta >= 1) {
                try {
                    update();
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                repaint();
                delta--;
            }
        }
    }
//to handle the movement of the car image ie to move the carforward ,backward,fire bullets etc
    void update() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        if (!gameRunning) {
            return;
        }
        if (carb) {
            imageX -= 3;
            imageX %= backgroundimg.getIconWidth();
            sound(carb);
            space = false;
        } else if (carf)
        {
            imageX += 3;
            imageX %= backgroundimg.getIconWidth();
            sound(carf);
        }

        if (carup) {
            if (ypos >= 20) {
                isJumping = true;
                carSpeedY = 5;
            } else {
                carSpeedY = 0;
            }
            //  sound(carup);
        }
        if (space) {
            bulletX += bulletSpeed;
            if (bulletX >= framewidth) {
                space = false; // Reset when the bullet has traveled the desired distance
                bulletX = xpos + 93; // Reset the bullet's starting position
            }
        }
        if (bomb) {
            bombdown=true;
        }
        updateBullets();


        // Handle flying enemy bullets
        handleFlyingEnemyBullets();
        for (int i = groundenemy.size() - 1; i >= 0; i--) {
            Rectangle groundenemyrect = groundenemy.get(i);
            Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());

            // Check if bullet collides with ground enemy
            updateGroundEnemyCollisions(i, groundenemyrect, bombrect);
        }

        for (int i = flyingenemy.size() - 1; i >= 0; i--) {
            Rectangle flyingenemyrect = flyingenemy.get(i);
            Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());
            updateFlyingEnemyCollisions(i, flyingenemyrect, bombrect);
        }

        //-----------------increase the number of enemies as distance increases______________
        int currentTime = elapsedTime; // You can also use elapsed time instead of distance
        if (currentTime - lastEnemySpawnTime >= enemySpawnInterval) {
            lastEnemySpawnTime = currentTime;

            // Add new ground enemies
            for (int i = 0; i < 3; i++) { // Add 3 new ground enemies each time
                int initialX = groundenemyx + i * 1000; // Adjust the initialX as needed
                groundenemy.add(new Rectangle(initialX, groundenemyy, groundenemyimage.getIconWidth(), groundenemyimage.getIconHeight()));
                groundenemyHitCount.add(0); // Reset hit count
            }

            // Add new flying enemies
            for (int i = 0; i < 3; i++) { // Add 3 new flying enemies each time
                Random random =new Random();
                int initialX = flyingenemyx + i * 1000; // Adjust the initialX as needed
                int initialY = random.nextInt(frameheight - flyingenemyimage.getIconHeight());
                flyingenemy.add(new Rectangle(initialX, initialY, flyingenemyimage.getIconWidth(), flyingenemyimage.getIconHeight()));
                flyingenemyHitCount.add(0); // Reset hit count
            }
        }
        groundcheckcollision();
        flyingcheckcollision();
    }

    private void handleFlyingEnemyBullets() {
        long currentTime = System.currentTimeMillis();
        for (Rectangle flyingenemyrect : flyingenemy) {
            if (flyingenemyrect != null) {
                double distanceToCar = calculateDistance(flyingenemyrect.x, flyingenemyrect.y, xpos, ypos);

                if (distanceToCar < 300) { // Adjust the distance threshold as needed
                    // Fire bullets
                    if (currentTime - lastBulletFireTime >= bulletFireInterval) {
                        lastBulletFireTime = currentTime;

                        int bulletX = flyingenemyrect.x - 10; // Adjust the bullet's starting position as needed
                        int bulletY = flyingenemyrect.y + flyingenemyimage.getIconHeight() / 2;
                        flyingEnemyBullets.add(new FlyingEnemyBullet(bulletX, bulletY, bulletSpeed));
                    }
                }
            }
        }

        Iterator<FlyingEnemyBullet> bulletIterator = flyingEnemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            FlyingEnemyBullet bullet = bulletIterator.next();
            bullet.move();
            Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());

            // Check for collisions with the car image
            if (carrect.intersects(new Rectangle(bullet.getX(), bullet.getY(), bulletimage.getIconWidth(), bulletimage.getIconHeight()))) {
                // Handle collision with the car image here (e.g., game over)
                gamerunning = false;
            }

            // Remove bullets that are out of bounds
            if (bullet.getX() < 0) {
                bulletIterator.remove();
            }
        }
    }

    //to measure the distance between the flying eney and car so that the enemy fires
    //only when it is near the car

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //detect collision between( bullets and ground enemy) &&(bombs and ground enemy)
    private void updateGroundEnemyCollisions(int index, Rectangle groundenemyrect, Rectangle collisionObject) {
        if (collisionObject.intersects(groundenemyrect))
        {
            groundenemyHitCount.set(index, groundenemyHitCount.get(index) + 1);
            // Remove the ground enemy if hit count reaches 3
            if (groundenemyHitCount.get(index) >= 3) {
                groundenemy.remove(index);
                groundenemyHitCount.remove(index);
                enemydeathcount++;
                explosion = true;
                explosionStartTime = System.currentTimeMillis();
                explosionX = groundenemyrect.x; // Store the explosion position
                explosionY = groundenemyrect.y;
                playExplosionSound();

                // Reset the hit count for other ground enemies to zero
                for (int j = 0; j < groundenemyHitCount.size(); j++) {
                    groundenemyHitCount.set(j, 0);
                }
            }
        }
    }
    //detect collision between( bullets and flying enemy) &&(bombs and flying enemy)

    private void updateFlyingEnemyCollisions(int index, Rectangle flyingenemyrect, Rectangle collisionObject) {
        if (collisionObject.intersects(flyingenemyrect)) {
            flyingenemyHitCount.set(index, flyingenemyHitCount.get(index) + 1);

            // Remove the flying enemy if hit count reaches 3
            if (flyingenemyHitCount.get(index) >= 3) {
                flyingenemy.remove(index);
                flyingenemyHitCount.remove(index);
                enemydeathcount++;
                explosion = true;
                explosionStartTime = System.currentTimeMillis();
                explosionX = flyingenemyrect.x; // Store the explosion position
                explosionY = flyingenemyrect.y;
                playExplosionSound();

                // Reset the hit count for other flying enemies to zero
                for (int j = 0; j < flyingenemyHitCount.size(); j++) {
                    flyingenemyHitCount.set(j, 0);
                }
            }
        }
    }

    //to check if the bullet has gone out of the frame if yes remove the bullet
    //and if bullet intersects the ground enemy call the checkcollision method()
    private void updateBullets() {
        Iterator<Rectangle> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Rectangle bullet = bulletIterator.next();
            bullet.x += bulletSpeed;

            if (bullet.x >= framewidth) {
                bulletIterator.remove(); // Remove the bullet if it's out of bounds
            }
            else {
                boolean bulletRemoved = false;
                for (int j = groundenemy.size() - 1; j >= 0; j--) {
                    Rectangle groundenemyrect = groundenemy.get(j);
                    if (groundenemyrect != null && bullet.intersects(groundenemyrect)) {
                        updateGroundEnemyCollisions(j, groundenemyrect, bullet);
                        bulletIterator.remove();  // Remove the bullet
                        bulletRemoved = true;
                        break;
                    }
                }
                if (!bulletRemoved) {
                    for (int j = flyingenemy.size() - 1; j >= 0; j--) {
                        Rectangle flyingenemyrect = flyingenemy.get(j);
                        if (flyingenemyrect != null && bullet.intersects(flyingenemyrect)) {
                            updateFlyingEnemyCollisions(j, flyingenemyrect, bullet);
                            bulletIterator.remove(); // Remove the bullet
                            break;
                        }
                    }
                }
            }
        }
    }
//to detect collision between car and flying enemy
    private void flyingcheckcollision() {
        Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());
        for (int i = 0; i < flyingenemy.size(); i++)
        {
            Rectangle flyingenemyrect = flyingenemy.get(i);
            if (flyingenemyrect != null && carrect.intersects(flyingenemyrect)) {
                gamerunning = false;
            }

        }
    }
    //to detect collision between car and ground enemy
    private void groundcheckcollision() {
        Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());

        for (int i = 0; i < groundenemy.size(); i++) {
            Rectangle groundenemyrect = groundenemy.get(i);
            if (groundenemyrect != null && carrect.intersects(groundenemyrect)) {
                gamerunning = false;
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
//to determine which key the user has pressed
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) {
            carf = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
           // carb = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (ypos > 20) {
                carup = true;
            } else {
                carSpeedY = 0;
            }
        }
        if (bulletCount > 0)
        {
            if (e.getKeyCode() == KeyEvent.VK_W)
            {
                carf = true;
            }
            // ... (other key handling code)
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !isSpaceKeyPressed) {
                isSpaceKeyPressed = true;
                bullets.add(new Rectangle(xpos + 80, ypos + 20, bulletimage.getIconWidth(), bulletimage.getIconHeight()));
                bulletCount--; // Decrease the bullet count when firing
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            bombxpos=xpos;
            bombypos=ypos;
            bomb = true;
            bombcount--;
        }
        if(bombcount<0)
        {
            bomb=false;
        }
        if (!gamerunning&&e.getKeyCode() == KeyEvent.VK_P) {
            changePlayerName();
        }
    }

    //to change the playersname each time game is over

    private void changePlayerName() {
        String newName = showCustomInputDialog();
        if (!newName.isEmpty()) {
            playerName = newName;
        }
    }

    //once a game is over to restart the game from beginning

    private void restartGame() {
        gamerunning = true;
        elapsedTime = 0;
        xpos = 300;
        ypos = 530;

        groundenemyx = 2000;
        groundenemyy = 445;
        dataAppended = false;

        enemydeathcount = 0;
        resetGroundEnemies();
        resetGroundEnemyHitCounts(); // Reset hit counts
        resetFlyingEnemies();
        resetFlyingEnemyHitCounts();
        bombcount=5;
        bulletCount=50;

        isJumping = false;
        carSpeedY = 0;
        space = false;
        bulletX = xpos + 93;
        bulletY = ypos + 20;

        lastEnemySpawnTime = 0;

        // Clear any existing bullets
        bullets.clear();
        repaint();
    }

    //once the game is over reset health of all the flying enemies

    private void resetFlyingEnemyHitCounts() {
        for (int i = 0; i < flyingenemyHitCount.size(); i++) {
            flyingenemyHitCount.set(i, 0);
        }
    }
    //once game is over remove all the flying enemies that is left

    private void resetFlyingEnemies() {
        flyingenemy.clear();
//        Random random = new Random();
//        for (int i = 0; i < 20; i++) {
//            int initialX = flyingenemyx + i * 1100;
//            int initialY = random.nextInt(frameheight - 200);
//            flyingenemy.add(new Rectangle(initialX, initialY, flyingenemyimage.getIconWidth(), flyingenemyimage.getIconHeight()));
//            flyingenemyHitCount.add(0); // Reset hit counts
//        }

    }
    //once the game is over reset health of all the ground enemies


    private void resetGroundEnemyHitCounts() {
        for (int i = 0; i < groundenemyHitCount.size(); i++) {
            groundenemyHitCount.set(i, 0);
        }
    }


    //to reset all the ground enemy to its initial position when game is over
    private void resetGroundEnemies() {
        //first ma all the existing ground enemy lai clear garni
        groundenemy.clear();

//        //add ground ememies back to the initial position
//        for (int i = 0; i <flyingenemyHitCount.size(); i++) {
//            int initialX = groundenemyx + i * 1000;
//            groundenemy.add(new Rectangle(initialX, groundenemyy, groundenemyimage.getIconWidth(), groundenemyimage.getIconHeight()));
//            groundenemyHitCount.add(0); // Reset hit counts
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            carf = false;
            sound(carf);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            carb = false;
            sound(carb);

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            carup = false;
            carSpeedY =- 5;
            gravity = 0.9; // Decrease the gravity

            //sound(carup);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            isSpaceKeyPressed = false;
        }
    }
    private void sound(boolean carsound) {
        try {
            obj.sound(carsound);
        } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            carSpeedY -= gravity;
            ypos -= carSpeedY;
            if (ypos >= 530) {
                ypos = 530;
                carSpeedY = 0;
                isJumping = false;
            }
        }
        if(bombdown)
        {
            if(bombypos<700) {
                bombypos += 10;
                System.out.println(ypos);
            }
            else {
                bomb=false;
                bombypos=ypos;
                bombdown=false;
            }
        }
        repaint();
    }
}