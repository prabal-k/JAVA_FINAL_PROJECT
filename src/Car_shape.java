import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

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
    ImageIcon backgroundimg = new ImageIcon("src\\background.png");
    private BufferedImage offscreenImage;
    private int imageX = 0;
    ImageIcon carimage = new ImageIcon("src\\car.png");
    ImageIcon flyingenemyimage = new ImageIcon("src\\flyingenemy.gif");
    ImageIcon groundenemyimage = new ImageIcon("src\\groundenemy.gif");
    ImageIcon bulletimage = new ImageIcon("src\\bullet.gif");
    ImageIcon forwardimage = new ImageIcon("src\\still.gif");
    ImageIcon backwardimage = new ImageIcon("src\\rstill.gif");
    ImageIcon explosionimage = new ImageIcon("src\\explosion.gif");
    ImageIcon bombimage = new ImageIcon("src\\bomb.gif");
    ImageIcon fly = new ImageIcon("src\\fly.gif");
    int xpos = 300, ypos = 590;
    Music obj = new Music();
    private Timer timer;
    private int carSpeedY = 0;
    private double gravity = 1;
    private boolean isJumping = false, gamerunning = true;
    //for score
    private int elapsedTime = 0;
    String formattedTime;
    int bombcount=5;
    private int bulletX = xpos + 80;
    private int bulletY = ypos + 20;
    private int bulletSpeed = 10;
    private int bulletDistance = 4000;
    int flyingenemyx = 2500, flyingenemyy = 200, groundenemyx = 2000, groundenemyy = 500;
    private boolean gameRunning = true;
    private boolean enterPressed = false;

    int bombxpos, bombypos ;
    private ArrayList<Rectangle> groundenemy = new ArrayList<>();
    private ArrayList<Integer> groundenemyHitCount = new ArrayList<>();
    private ArrayList<Rectangle> flyingenemy = new ArrayList<>();
    private ArrayList<Integer> flyingenemyHitCount = new ArrayList<>();
    int enemydeathcount = 0;
    int highestKilled = 0;
    String time = "", username = "";
    boolean bombdown=false;
    private boolean explosion = false;
    private long explosionStartTime = 0;
    private int explosionX, explosionY;
    private ArrayList<Rectangle> bullets = new ArrayList<>();



    public Car_shape() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        Timer htimer = new Timer(1000, new ActionListener() { // Timer fires every 1000 milliseconds (1 second)
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++; // Increment the elapsed time every second
            }
        });
        htimer.start();
        Timer timer1 = new Timer(16, this);
        timer1.start();
        playerName = JOptionPane.showInputDialog("Enter your name:");
        //for ground enemy position
        for(int i=0;i<20000;i=i+1500) {
            groundenemy.add(new Rectangle(groundenemyx+i, groundenemyy, groundenemyimage.getIconWidth(), groundenemyimage.getIconHeight()));
        }
        for(int i=2000;i<20000;i=i+2000) {
            //for flying enemy position
            flyingenemy.add(new Rectangle(flyingenemyx+i, flyingenemyy, flyingenemyimage.getIconWidth(), flyingenemyimage.getIconHeight()));
        }


        for (int i = 0; i < groundenemy.size(); i++) {
            groundenemyHitCount.add(0);
        }

        for (int i = 0; i < flyingenemy.size(); i++) {
            flyingenemyHitCount.add(0);
        }

    }

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

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (offscreenImage == null) {
            offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2d = offscreenImage.createGraphics();
        int imageWidth = backgroundimg.getIconWidth();
        // Clear the offscreen buffer
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the background image
        g2d.drawImage(backgroundimg.getImage(), -imageX, 0, imageWidth, getHeight(), this);
        g2d.drawImage(backgroundimg.getImage(), imageWidth - imageX, 0, imageWidth, getHeight(), this);

        // Draw score in mm:ss format
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Ink free", Font.BOLD, 30));
        formattedTime = String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60);
        g2d.drawString("Time: " + formattedTime, 10, 30);
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


        //draw ground enemy
        for (Rectangle groundenemyimage1 : groundenemy) {
            if (groundenemyimage1 != null) {
                if (carf) {
                    groundenemyimage1.x -= 3;
                } else if (carb) {
                    groundenemyimage1.x += 1;
                } else {
                    groundenemyimage1.x -= 1;
                }
                g2d.drawImage(groundenemyimage.getImage(), groundenemyimage1.x, groundenemyimage1.y, this);
            }
        }

        //draw flying enemy
        for (Rectangle flyingenemyimage1 : flyingenemy) {
            if (flyingenemyimage1 != null) {
                if (carf) {
                    flyingenemyimage1.x -= 3;
                } else if (carb) {
                    flyingenemyimage1.x += 1;
                } else {
                    flyingenemyimage1.x -= 1;
                }
                g2d.drawImage(flyingenemyimage.getImage(), flyingenemyimage1.x, flyingenemyimage1.y, this);
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
            g2d.drawImage(explosionimage.getImage(), explosionX, explosionY, this);

        }

        else {
            explosionStartTime=0;
        }
        for (Rectangle bullet : bullets) {
            g2d.drawImage(bulletimage.getImage(), bullet.x, bullet.y, this);
        }

        g2d.drawImage(bombimage.getImage(),700,0,null);

        groundcheckcollision();
        g2d.dispose(); // Release resources
        g.drawImage(offscreenImage, 0, 0, this);
    }

    private void gameover(Graphics g) throws IOException {
        if (!dataAppended) {
            File file = new File("Score.txt");

            //TO WRITE IN FILE
            FileWriter filewriter = new FileWriter(file, true);
            filewriter.write("Killed: " + enemydeathcount + " ");
            filewriter.write(formattedTime + " ");
            filewriter.write(playerName + "\n");
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
        g.drawString("Game Over", 450, 100);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Killed: " + enemydeathcount, 500, 200);
        g.drawString("Best score: " + "Killed" + String.valueOf(highestKilled), 50, 50);
        g.drawString(time, 50, 100);
        g.drawString(username, 00, 150);
        g.drawString("Press Enter to Restart", 500, 400);
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

    void update() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        if (!gameRunning) {
            return; // Skip updating if the game is over
        }
        if (carb) {
            imageX -= 3;
            imageX %= backgroundimg.getIconWidth();
            sound(carb);
            space = false;
        } else if (carf) {
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
        for (int i = 0; i < bullets.size(); i++) {
            Rectangle bullet = bullets.get(i);
            bullet.x += bulletSpeed;

            if (bullet.x >= framewidth) {
                bullets.remove(i);
                i--;
            }

            // Check collisions with enemies (ground or flying)
            for (int j = 0; j < groundenemy.size(); j++) {

                Rectangle groundenemyrect = groundenemy.get(j);
                Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());
                if (bullet.intersects(groundenemyrect)) {
                    System.out.println(groundenemyHitCount.get(i));
                    bullets.remove(i);
                    groundenemyHitCount.set(i, groundenemyHitCount.get(i) + 1);

                    // Remove the ground enemy if hit count reaches 10
                    if (groundenemyHitCount.get(i) >= 3) {
                        groundenemy.remove(i);
                        groundenemyHitCount.remove(i);
                        enemydeathcount++;
                        explosion=true;
                        explosionStartTime = System.currentTimeMillis();
                        explosionX = groundenemyrect.x; // Store the explosion position
                        explosionY = groundenemyrect.y;

                        // Reset the hit count for other ground enemies to zero
                        for (int k = 0; k < groundenemyHitCount.size(); k++) {
                            groundenemyHitCount.set(k, 0);
                        }

                    }
                    space = false;
                }

            }

            for (int j = 0; j < flyingenemy.size(); j++) {
                Rectangle flyingenemyrect = flyingenemy.get(j);
                Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());

                if (bullet.intersects(flyingenemyrect)) {
                    flyingenemyHitCount.set(i, flyingenemyHitCount.get(i) + 1);
                    bullets.remove(i);
                    flyingenemyHitCount.set(i,flyingenemyHitCount.get(i) + 1);

                    if (flyingenemyHitCount.get(i) >= 3) {
                            flyingenemy.remove(i);
                            flyingenemyHitCount.remove(i);
                            enemydeathcount++;
                            explosion=true;
                            explosionStartTime = System.currentTimeMillis();
                            explosionX = flyingenemyrect.x; // Store the explosion position
                            explosionY = flyingenemyrect.y;

                            // Reset the hit count for other ground enemies to zero
                            for (int k = 0; k < flyingenemyHitCount.size(); k++) {
                                flyingenemyHitCount.set(k, 0);
                            }

                    }

                }
            }
        }

        groundcheckcollision();
        flyingcheckcollision();
    }

    private void flyingcheckcollision() {
        Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());
        Rectangle bulletrect = new Rectangle(bulletX, bulletY, bulletimage.getIconWidth(), bulletimage.getIconHeight());
        Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());

        for (int i = 0; i < flyingenemy.size(); i++) {
            Rectangle flyingenemyrect = flyingenemy.get(i);
            if (flyingenemyrect != null && carrect.intersects(flyingenemyrect)) {
                gamerunning = false;
            }

            if (bomb&&bombrect.intersects(flyingenemyrect)) {
                flyingenemyHitCount.set(i, flyingenemyHitCount.get(i)+1 );
                if (flyingenemyHitCount.get(i) >= 1) {
                    flyingenemy.remove(i);
                    flyingenemyHitCount.remove(i);
                    enemydeathcount++;
                    explosion=true;
                    explosionStartTime = System.currentTimeMillis();
                    explosionX = flyingenemyrect.x; // Store the explosion position
                    explosionY = flyingenemyrect.y;

                    // Reset the hit count for other ground enemies to zero
                    for (int j = 0; j < flyingenemyHitCount.size(); j++) {
                        flyingenemyHitCount.set(j, 0);
                    }

                }
            }
//            if (space && bulletrect.intersects(flyingenemyrect))
//            {
//                // Increment the hit count for this ground enemy
////                System.out.println(groundenemyHitCount.get(i));
//                flyingenemyHitCount.set(i, flyingenemyHitCount.get(i) + 1);
//
//                // Remove the ground enemy if hit count reaches 10
//                if (flyingenemyHitCount.get(i) >= 3) {
//                    flyingenemy.remove(i);
//                    flyingenemyHitCount.remove(i);
//                    enemydeathcount++;
//                    explosion=true;
//                    explosionStartTime = System.currentTimeMillis();
//                    explosionX = flyingenemyrect.x; // Store the explosion position
//                    explosionY = flyingenemyrect.y;
//
//                    // Reset the hit count for other ground enemies to zero
//                    for (int j = 0; j < flyingenemyHitCount.size(); j++) {
//                        flyingenemyHitCount.set(j, 0);
//                    }
//
//                }
//                space = false;
//            }
        }

    }

    private void groundcheckcollision() {
        Rectangle carrect = new Rectangle(xpos, ypos, carimage.getIconWidth(), carimage.getIconHeight());
        Rectangle bulletrect = new Rectangle(bulletX, bulletY, bulletimage.getIconWidth(), bulletimage.getIconHeight());
        Rectangle bombrect = new Rectangle(bombxpos, bombypos, bombimage.getIconWidth(), bombimage.getIconHeight());

        for (int i = 0; i < groundenemy.size(); i++) {
            Rectangle groundenemyrect = groundenemy.get(i);
            if (groundenemyrect != null && carrect.intersects(groundenemyrect)) {
                gamerunning = false;
            }

            if (bomb&&bombrect.intersects(groundenemyrect)) {
                groundenemyHitCount.set(i, groundenemyHitCount.get(i)+1 );
                if (groundenemyHitCount.get(i) >= 1) {
                    groundenemy.remove(i);
                    groundenemyHitCount.remove(i);
                    enemydeathcount++;
                    explosion=true;
                    explosionStartTime = System.currentTimeMillis();
                    explosionX = groundenemyrect.x; // Store the explosion position
                    explosionY = groundenemyrect.y;

                    // Reset the hit count for other ground enemies to zero
                    for (int j = 0; j < groundenemyHitCount.size(); j++) {
                        groundenemyHitCount.set(j, 0);
                    }

                }
            }
//            if (space && bulletrect.intersects(groundenemyrect)) {
//                // Increment the hit count for this ground enemy
//                System.out.println(groundenemyHitCount.get(i));
//                groundenemyHitCount.set(i, groundenemyHitCount.get(i) + 1);
//
//                // Remove the ground enemy if hit count reaches 10
//                if (groundenemyHitCount.get(i) >= 3) {
//                    groundenemy.remove(i);
//                    groundenemyHitCount.remove(i);
//                    enemydeathcount++;
//                    explosion=true;
//                    explosionStartTime = System.currentTimeMillis();
//                    explosionX = groundenemyrect.x; // Store the explosion position
//                    explosionY = groundenemyrect.y;
//
//                    // Reset the hit count for other ground enemies to zero
//                    for (int j = 0; j < groundenemyHitCount.size(); j++) {
//                        groundenemyHitCount.set(j, 0);
//                    }
//
//                }
//                space = false;
//            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) {
            carf = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            carb = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (ypos > 20) {
                carup = true;
            } else {
                carSpeedY = 0;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//            space = true;
            bullets.add(new Rectangle(xpos + 80, ypos + 20, bulletimage.getIconWidth(), bulletimage.getIconHeight()));
//            bulletX = xpos + 80; // Reset the bullet's starting position
//            bulletY = ypos + 20;

        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            bombxpos=xpos;
            bombypos=ypos;
            bomb = true;
            bombcount--;
        }
        if(bombcount<0)
        {
            bomb=false;
        }
        

    }

    private void restartGame() {
        gamerunning = true;
        elapsedTime = 0;
        xpos = 300;
        ypos = 590;

        groundenemyx = 2000;
        groundenemyy = 500;
        dataAppended = false;

        enemydeathcount = 0;
        resetGroundEnemies();
        resetGroundEnemyHitCounts(); // Reset hit counts
        resetFlyingEnemies();
        resetFlyingEnemyHitCounts();
        bombcount=5;

        isJumping = false;
        carSpeedY = 0;
        space = false;
        bulletX = xpos + 93;
        bulletY = ypos + 20;
        repaint();
    }

    private void resetFlyingEnemyHitCounts() {
        for (int i = 0; i < flyingenemyHitCount.size(); i++) {
            flyingenemyHitCount.set(i, 0);
        }
    }

    private void resetFlyingEnemies() {
        flyingenemy.clear();

        //add ground ememies back to the initial position
        for (int i = 0; i < 20; i++) {
            int initialX = flyingenemyx + i * 2000;
            flyingenemy.add(new Rectangle(initialX, flyingenemyy,flyingenemyimage.getIconWidth(), flyingenemyimage.getIconHeight()));
            flyingenemyHitCount.add(0); // Reset hit counts
        }

    }

    private void resetGroundEnemyHitCounts() {
        for (int i = 0; i < groundenemyHitCount.size(); i++) {
            groundenemyHitCount.set(i, 0);
        }
    }


    //to reset all the ground enemy to its initial position when game is over
    private void resetGroundEnemies() {
        //first ma all the existing ground enemy lai clear garni
        groundenemy.clear();

        //add ground ememies back to the initial position
        for (int i = 0; i <20; i++) {
            int initialX = groundenemyx + i * 2000;
            groundenemy.add(new Rectangle(initialX, groundenemyy, groundenemyimage.getIconWidth(), groundenemyimage.getIconHeight()));
            groundenemyHitCount.add(0); // Reset hit counts
        }


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
//        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//            space = false;
//        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
//        if (e.getKeyCode() == KeyEvent.VK_UP) {
//            bomb = false;
//            bombypos = ypos;
//            // bombxpos=xpos;
//        }
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
            if (ypos >= 600) {
                ypos = 600;
                carSpeedY = 0;
                isJumping = false;
            }
        }
        if(bombdown)
        {
            if(bombypos<700) {
                bombypos += 20;
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