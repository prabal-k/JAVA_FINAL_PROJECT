
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
    //for polygon (car shape)//y positions :513 513 535 540 555 555 539 535
    int y3 = 108, y4 = 108, y5 = 130, y6 = 135, y7 = 150, y8 = 150, y9 = 134, y10 = 130;
    //for wheel
    int yb1 = 140,  yb2 = 140;
    //for game control loop (fps)
    int fps = 60;
    Thread gameThread;
    boolean carf = false, carb = false, carup = false, cardown = false, esc = false,space=false;
    int framewidth = 1200, frameheight = 700;
    int initialspeed = 10;
    int sspeed = 5,sspeed2=3;
    //for background image
    ImageIcon backgroundimg = new ImageIcon("src\\background.png");
    private BufferedImage offscreenImage;
    private int imageX = 0;
    ImageIcon img2 = new ImageIcon("src\\Cardesign.png");
    ImageIcon fly = new ImageIcon("src\\fly.gif");
    ImageIcon img4 = new ImageIcon("src\\Cardesign3.png");
    ImageIcon img6 = new ImageIcon("src\\fire.png");
    ImageIcon coinimage = new ImageIcon("src\\coin1.gif");
    ImageIcon setting = new ImageIcon("src\\Setting.png");
    int gcount = 0;
    int backgroundOffsetX = 0;
    int carSpeed = 0;
    int xpos = 0, ypos = 0;
    int s1 = 200, s2 = 800, s3 = 200, s4 = 800;
    int t1 = 480, t2 = 480, t3 = 520, t4 = 520;
    Music obj = new Music();
    int surfacespeed = 0,surfacespeed2=0;
    private Image[] images;
    private Image[] imagesd;
    private int currentIndex = 0;
    private boolean aKeyPressed = false;
    private Timer timer;
    private long keyAPressedStartTime;
    private double keyAPressedDuration = 0;
    private boolean dKeyPressed = false;
    private long keydPressedStartTimed;
    private double keyAPressedDurationd = 0;
    private int carSpeedY = 0;
    private int gravity = 1;
    private boolean isJumping = false;

    // New variables for the moving rectangle
    private int rectX = 0;
    private int rectY = 450;
    private int rectSpeedX = 8; // Adjust the speed as need
    private int rectDrawCounter = 0;
    private int rectDrawDelay = 300; // Number of game cycles before redrawing t
    private int carWidth = img4.getIconWidth();
    private int carHeight = img4.getIconHeight();
    int rectwidth = 40, rectheight = 20;
    //for score
    private int elapsedTime = 0;
    private boolean gameOver = false;
    protected Rectangle carImageRect, enemyRect;
    boolean gamerun=true;
    String formattedTime;
    private ArrayList<Point> coins = new ArrayList<>();
    private ArrayList<Ramp> ramps = new ArrayList<>();
    int coinscore=0;
    public Car_shape() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        // Initialize the Timer with a delay of 1000ms (1 second)
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aKeyPressed) {
                    keyAPressedDuration += 0.01; // Increment the duration by 1 second
                }
                if (dKeyPressed) {
                    keyAPressedDurationd += 0.01; // Increment the duration by 1 second
                }
            }
        });
        Timer htimer = new Timer(1000, new ActionListener() { // Timer fires every 1000 milliseconds (1 second)
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++; // Increment the elapsed time every second
            }
        });
        htimer.start();
        Timer timer1 = new Timer(16, this);
        timer1.start();
        //userName = JOptionPane.showInputDialog(this, "Please enter your name:");

        //frame.add(background);
        //A key
        images = new Image[15];
        images[0] = new ImageIcon("src\\up1.png").getImage();  // Load your images here
        images[1] = new ImageIcon("src\\up2.png").getImage();  // Load your images here
        images[2] = new ImageIcon("src\\up3.png").getImage();  // Load your images here
        images[3] = new ImageIcon("src\\up4.png").getImage();  // Load your images here
        images[4] = new ImageIcon("src\\up5.png").getImage();  // Load your images here
        images[5] = new ImageIcon("src\\up6.png").getImage();  // Load your images here
        images[6] = new ImageIcon("src\\up7.png").getImage();  // Load your images here
        images[7] = new ImageIcon("src\\up8.png").getImage();
        images[8] = new ImageIcon("src\\up9.png").getImage();
        images[9] = new ImageIcon("src\\up10.png").getImage();
        images[10] = new ImageIcon("src\\up11.png").getImage();
        images[11] = new ImageIcon("src\\up12.png").getImage();  // Load your images here
        images[12] = new ImageIcon("src\\up13.png").getImage();  // Load your images here
        images[13] = new ImageIcon("src\\up14.png").getImage();  // Load your images here
        images[14] = new ImageIcon("src\\up15.png").getImage();  // Load your images here
        //D key
        imagesd = new Image[10];
        imagesd[0] = new ImageIcon("src\\down1.png").getImage();
        imagesd[1] = new ImageIcon("src\\down2.png").getImage();
        imagesd[2] = new ImageIcon("src\\down3.png").getImage();
        imagesd[3] = new ImageIcon("src\\down4.png").getImage();
        imagesd[4] = new ImageIcon("src\\down5.png").getImage();
        imagesd[5] = new ImageIcon("src\\down6.png").getImage();
        imagesd[6] = new ImageIcon("src\\down7.png").getImage();
        imagesd[7] = new ImageIcon("src\\down8.png").getImage();
        imagesd[8] = new ImageIcon("src\\down9.png").getImage();
        imagesd[9] = new ImageIcon("src\\down10.png").getImage();
        //coin
        coins.add(new Point(1000, 300));
        coins.add(new Point(1300, 300));
        coins.add(new Point(1600, 300));

        coins.add(new Point(3000, 200));
        coins.add(new Point(3200, 200));
        coins.add(new Point(3400, 200));
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
    public void paint(Graphics g) {
        super.paint(g);

        if (offscreenImage == null) {
            offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2d = (Graphics2D) g;
        int imageWidth = backgroundimg.getIconWidth();

        // Clear the offscreen buffer
        g2d.setColor(getBackground());
       // g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the background image
        g2d.drawImage(backgroundimg.getImage(), -imageX, 0, imageWidth, getHeight(), this);
        g2d.drawImage(backgroundimg.getImage(), imageWidth - imageX, 0, imageWidth, getHeight(), this);

        if(gamerun)
        {
        int wid = img4.getIconWidth();
        int hei = img4.getIconHeight();
        // System.out.println("wid = " + wid + " hei = " + hei);
        carImageRect = new Rectangle(xpos+310, ypos+214, wid, hei);

        // Draw enemy moving rectangle
        g.setColor(Color.BLUE);
        g.fillRect(rectX, rectY, rectwidth, rectheight);
            for (Point coin : coins) {
                if (coin != null) {
                     coin.x-=surfacespeed2;
                        g2d.drawImage(coinimage.getImage(), coin.x, coin.y, this);
                }
            }
            //surface
        Stroke customStroke = new BasicStroke(5f); // Set the desired thickness (5 pixels in this example)
        g2d.setStroke(customStroke);
        // Draw the same polygon multiple times with gaps in between
        int gap = 100; // Set the gap between the polygons
        int distance = 620; // Set the distance between the polygons

        // Draw score in mm:ss format
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
        formattedTime = String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60);
        g.drawString("Score: " + formattedTime, 10, 30);



    if (carup) {

                currentIndex = (currentIndex + 1) % images.length;
                if (keyAPressedDuration < 0.04) {
                    g2d.drawImage(images[0], xpos + 73, ypos - 1, this);
                }
                if (keyAPressedDuration >= 0.04 && keyAPressedDuration <= 0.07) {
                    g2d.drawImage(images[1], xpos + 73, ypos - 1, this);
                }
                if (keyAPressedDuration >= 0.07 && keyAPressedDuration <= 0.11) {
                    g2d.drawImage(images[2], xpos + 72, ypos - 3, this);
                }
                if (keyAPressedDuration >= 0.11 && keyAPressedDuration <= 0.14) {
                    g2d.drawImage(images[3], xpos + 72, ypos - 5, this);
                }
                if (keyAPressedDuration >= 0.14 && keyAPressedDuration <= 0.17) {
                    g2d.drawImage(images[4], xpos + 71, ypos - 7, this);
                }
                if (keyAPressedDuration >= 0.17 && keyAPressedDuration <= 0.20) {

                    g2d.drawImage(images[5], xpos + 71, ypos - 9, this);
                }
                if (keyAPressedDuration >= 0.20 && keyAPressedDuration <= 0.23) {
                    g2d.drawImage(images[6], xpos + 70, ypos - 10, this);
                }
                if (keyAPressedDuration >= 0.23 && keyAPressedDuration <= 0.26) {
                    g2d.drawImage(images[7], xpos + 70, ypos - 10, this);
                }
                if (keyAPressedDuration >= 0.26 && keyAPressedDuration <= 0.29) {
                    g2d.drawImage(images[8], xpos + 70, ypos - 13, this);
                }
                if (keyAPressedDuration >= 0.29 && keyAPressedDuration <= 0.32) {
                    g2d.drawImage(images[9], xpos + 69, ypos - 15, this);
                }
                if (keyAPressedDuration >= 0.32 && keyAPressedDuration <= 0.35) {
                    g2d.drawImage(images[11], xpos + 69, ypos - 17, this);
                }
                if (keyAPressedDuration >= 0.35 && keyAPressedDuration <= 0.39) {
                    g2d.drawImage(images[12], xpos + 68, ypos - 19, this);
                }
                if (keyAPressedDuration >= 0.39 && keyAPressedDuration <= 0.43) {
                    g2d.drawImage(images[13], xpos + 68, ypos - 21, this);
                }
                if (keyAPressedDuration >= 0.43 ) {
                    g2d.drawImage(images[14], xpos + 68, ypos - 23, this);
                    gamerun=false;
                    gameOver=true;
                    if(gameOver)
                    {
                        try {
                            gameover();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
                }
            } else if (carf) {
                g2d.drawImage(img2.getImage(), xpos + 20, ypos, null);
                g2d.drawImage(img6.getImage(), xpos + 110, ypos + 185, null);
            } else if (carb) {
                g2d.drawImage(img2.getImage(), xpos, ypos - 2, null);
                g2d.drawImage(img6.getImage(), xpos + 90, ypos + 185, null);

            } else if (cardown) {
                    if (keyAPressedDurationd < 0.05) {
                        g2d.drawImage(imagesd[0], xpos + 42, ypos + 28, this);;
                    }
                    if (keyAPressedDurationd >= 0.05 && keyAPressedDurationd <= 0.09) {
                        g2d.drawImage(imagesd[1], xpos + 45, ypos + 22, this);
                    }
                    if (keyAPressedDurationd >= 0.09 && keyAPressedDurationd <= 0.13) {

                        g2d.drawImage(imagesd[2], xpos + 48, ypos + 16, this);
                    }
                    if (keyAPressedDurationd >= 0.13 && keyAPressedDurationd <= 0.17) {
                        g2d.drawImage(imagesd[3], xpos + 50, ypos + 16, this);
                    }
                    if (keyAPressedDurationd >= 0.17 && keyAPressedDurationd <= 0.2) {
                        g2d.drawImage(imagesd[4], xpos + 52, ypos + 13, this);
                    }
                    if (keyAPressedDurationd >= 0.2&& keyAPressedDurationd <= 0.23) {
                        g2d.drawImage(imagesd[5], xpos + 54, ypos + 10, this);
                    }
                     if (keyAPressedDurationd >= 0.23&& keyAPressedDurationd <= 0.26) {
                        g2d.drawImage(imagesd[6], xpos + 55, ypos + 8, this);
                    }
                    if (keyAPressedDurationd >= 0.26&& keyAPressedDurationd <= 0.28) {
                        g2d.drawImage(imagesd[7], xpos + 56, ypos + 8, this);

                }
                if (keyAPressedDurationd >= 0.28&& keyAPressedDurationd <= 0.30) {
                    g2d.drawImage(imagesd[8], xpos + 56, ypos + 7, this);

                }
                if (keyAPressedDurationd >= 0.30) {
                    g2d.drawImage(imagesd[9], xpos + 56, ypos + 7, this);
                    gamerun=false;
                    gameOver=true;
                    if(gameOver==true)
                    {
                        try {
                            gameover();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            else
            {
                g2d.drawImage(img4.getImage(),  310, ypos + 214, null);
            }
            if(space)
            {
                g2d.drawImage(fly.getImage(),xpos+370,ypos+275,null);
            }
        enemyRect = new Rectangle(rectX, rectY, rectwidth, rectheight);
        if (carImageRect.intersects(enemyRect)) {
//            gamerun = false;
//            gameOver =true;
//            if(gameOver==true)
//            {
//                try {
//                    gameover();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//
       }
        }

        g.setFont(new Font("INK free", Font.BOLD, 30));
        g.drawString(String.valueOf(coinscore),400,50);
        g2d.dispose(); // Release resources

        g.drawImage(offscreenImage, 0, 0, this);
    }
    private void gameover() throws IOException {
        frame.dispose();
       carup=false;
       cardown=false;
        keyAPressedDuration=0;
        keyAPressedDurationd=0;
        GameOver over =new GameOver();
        try {
            over.overdetail(formattedTime,coinscore);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                checkCollision();
                delta--;
            }
        }
    }
    void update() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        backgroundOffsetX -= carSpeed;
        s1 -= surfacespeed;
        s2 -= surfacespeed;
        s3 -= surfacespeed;
        s4 -= surfacespeed;
        //car initially falling animation
        for (int k = y3 - 100; k <= 200; k = k + 18) {
            ypos += 15;
            y3 = y3 + 15;
            y4 = y4 + 15;
            y5 = y5 + 15;
            y6 = y6 + 15;
            y7 = y7 + 15;
            y8 = y8 + 15;
            y9 = y9 + 15;
            y10 = y10 + 15;
            yb1 = yb1 + 15;
            yb2 = yb2 + 15;
            try {
                Thread.sleep(30);
                repaint();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            carf = true;
            gcount = 1;
            imageX += 10;
            imageX %= backgroundimg.getIconWidth();

            try {
                obj.sound(carf);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            carSpeed = initialspeed;
            surfacespeed = sspeed;
            surfacespeed2 = sspeed2;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            carb = true;
            carSpeed = -initialspeed;
            surfacespeed = -sspeed;
            surfacespeed2 -= sspeed2;
            imageX -= 10;
            imageX %= backgroundimg.getIconWidth();
            try {
                obj.sound(carb);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            carup = true;
            aKeyPressed = true;
            keyAPressedStartTime = System.currentTimeMillis();
            timer.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            cardown = true;
            dKeyPressed = true;
            keydPressedStartTimed = System.currentTimeMillis();
            timer.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            esc = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isJumping) {
            isJumping = true;
            space=true;
            carSpeedY = 15;
        }

    }
    private void checkCollision() {
        for (int i = 0; i < coins.size(); i++) {
            Point coin = coins.get(i);
            if (coin != null) {
                Rectangle coinBounds = new Rectangle(coin.x, coin.y, coinimage.getIconWidth(), coinimage.getIconHeight());
                    if (carImageRect.intersects(coinBounds)) {
                        coins.set(i, null);
                        coinscore++;
                    }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S ) {
            carf = false;
            try {
                obj.sound(carf);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            carb = false;
            carSpeed = 0;
            surfacespeed = 0;
            surfacespeed2 = 0;
            gcount = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            carup = false;
            aKeyPressed = false;
            // Calculate the time duration for which "A" key was pressed and reset the timer
            keyAPressedDuration = 0;
            keyAPressedDuration += (System.currentTimeMillis() - keyAPressedStartTime) / 1000;
            keyAPressedStartTime = 0;
            timer.stop(); // Stop the Timer when "A" key is released
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            cardown = false;
            dKeyPressed = false;
            // Calculate the time duration for which "A" key was pressed and reset the timer
            keyAPressedDurationd = 0;
            keyAPressedDurationd += (System.currentTimeMillis() - keydPressedStartTimed) / 1000;
            keydPressedStartTimed = 0;
            timer.start();
        }
        if (e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            space=false;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            carSpeedY -= gravity;
            ypos -= carSpeedY;
            if (ypos >= 195) {
                ypos = 195;
                carSpeedY = 0;
                isJumping = false;
            }
        }
        // Update game logic for moving rectangle
        rectX += rectSpeedX;
        if (rectX >= getWidth()) {
            rectDrawCounter++;
            if (rectDrawCounter >= rectDrawDelay) {
                rectX = 0;
                ; // Reset to the right end
                rectDrawCounter = 0;
            }
        }
        repaint();
    }


}