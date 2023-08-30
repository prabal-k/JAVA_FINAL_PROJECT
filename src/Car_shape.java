
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
public class Car_shape extends JPanel implements KeyListener, ActionListener,Runnable {
    JFrame frame = new JFrame();
    int fps = 60;
    Thread gameThread;
    boolean carf = false, carb = false, carup = false,space=false;
    int framewidth = 1200, frameheight = 700;
    //for background image
    ImageIcon backgroundimg = new ImageIcon("src\\background.png");
    private BufferedImage offscreenImage;
    private int imageX = 0;
    ImageIcon carimage = new ImageIcon("src\\car.png");
    ImageIcon flyingenemyimage = new ImageIcon("src\\enemy.gif");
    ImageIcon groundenemyimage = new ImageIcon("src\\groundenemy.gif");
    ImageIcon bulletimage = new ImageIcon("src\\bullet.gif");

    ImageIcon forwardimage = new ImageIcon("src\\still.gif");
    ImageIcon backwardimage = new ImageIcon("src\\rstill.gif");
    ImageIcon fly = new ImageIcon("src\\fly.gif");
    int xpos = 300, ypos = 590;
    Music obj = new Music();
    private Timer timer;
    private int carSpeedY = 0;
    private int gravity = 1;
    private boolean isJumping = false,gamerunning=true;

    //for score
    private int elapsedTime = 0;
    String formattedTime;
    private int bulletX = xpos + 93;
    private int bulletY = ypos + 20;
    private int bulletSpeed =10;
    int flyingenemyx=2000,flyingenemyy=200,groundenemyx=1000,groundenemyy=550;
    private boolean gameRunning = true;
    private boolean enterPressed = false;
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
        
        //Graphics2D g2d = (Graphics2D) g;
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
        g2d.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
        formattedTime = String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60);
        g2d.drawString("Score: " + formattedTime, 10, 30);

        if (carf)
        {
            g2d.drawImage(forwardimage.getImage(),  xpos, ypos , null);

        }
        else if (carb)
        {
            g2d.drawImage(backwardimage.getImage(),  xpos, ypos , null);

        }
        else if (carup) {
            g2d.drawImage(forwardimage.getImage(),  xpos, ypos , null);
            g2d.drawImage(fly.getImage(),xpos+35,ypos+30,null);

        }

        else
         {
             g2d.drawImage(carimage.getImage(),  xpos, ypos , null);
         }
        if (space)
        {
            g2d.drawImage(bulletimage.getImage(),  bulletX, bulletY , null);

        }



        //for flyingEnemy CHaracter
        g2d.drawImage(flyingenemyimage.getImage(),flyingenemyx,flyingenemyy,null);

        //for groundEnemy CHaracter
        g2d.drawImage(groundenemyimage.getImage(),groundenemyx,groundenemyy,null);

        //check collision between car and flyingenemy
        Rectangle carrect =new Rectangle(xpos,ypos,carimage.getIconWidth(),carimage.getIconHeight());
        Rectangle enemyRect =new Rectangle(flyingenemyx,flyingenemyy,flyingenemyimage.getIconWidth(),flyingenemyimage.getIconHeight());
        if(carrect.intersects(enemyRect))
            {
                gamerunning=false;
            }

        //check collision between car and ground enemy
        Rectangle groundenemyrect =new Rectangle(groundenemyx,groundenemyy,groundenemyimage.getIconWidth(),groundenemyimage.getIconHeight());
        if(carrect.intersects(groundenemyrect))
        {
            gamerunning=false;
            
        }
        if (!gamerunning) {
            gameover(g);
            return;
        }

        g2d.dispose(); // Release resources
        g.drawImage(offscreenImage, 0, 0, this);
    }

    private void gameover(Graphics g) {
        carf = false;
        carb = false;
        carup = false;
        space = false;
        sound(false);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", framewidth / 2 - 100, frameheight / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press Enter to Restart", framewidth / 2 - 120, frameheight / 2 + 40);
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
        flyingenemyx--;
        groundenemyx--;
        if (!gameRunning) {
            return; // Skip updating if the game is over
        }

        // Existing update logic


        if (space)
        {
            bulletX += bulletSpeed;
            if (bulletX > framewidth) {
              space = false; // Reset when bullet reaches the right end
            }
        }
        if (carf)
        {
            imageX += 4;
            imageX %= backgroundimg.getIconWidth();
            sound(carf);
            flyingenemyx=flyingenemyx-3;
             groundenemyx=groundenemyx-3;
        }
        if(carb)
        {
            imageX -= 4;
            imageX %= backgroundimg.getIconWidth();
            sound(carb);
        }
        if(carup)
        {
            isJumping = true;
            carSpeedY = 6;
            flyingenemyy++;
        }
//

    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W ) {
            carf = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            carb = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_A) {

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {

            carup = true;


        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            space=true;
            bulletX = xpos + 93; // Reset the bullet's starting position
            bulletY = ypos + 20;

        }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    enterPressed = true; // Set the flag to true
                if(!gamerunning) {
                    restartGame();
                }

            }

    }

    private void restartGame() {
        gamerunning=true;
        elapsedTime = 0;
        xpos = 300;
        ypos = 590;
        flyingenemyx = 2000;
        flyingenemyy = 200;
        groundenemyx = 1000;
        groundenemyy = 550;
        isJumping = false;
        carSpeedY = 0;
        space = false;
        bulletX = xpos + 93;
        bulletY = ypos + 20;
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W ) {
            carf = false;
            sound(carf);
        }
        if (e.getKeyCode()== KeyEvent.VK_S )
        {
            carb = false;
            sound(carb);

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            carup = false;
            carSpeedY = 6;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            space=false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            enterPressed = false;
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
            if (ypos >= 600) {
                ypos = 600;
                carSpeedY = 0;
                isJumping = false;
            }
        }

        repaint();
    }


}