import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MenuClass extends JPanel implements ActionListener {

    private JFrame frame = new JFrame("Game Main Menu");
    private JButton startButton = new JButton("Start");
    private JButton exitButton = new JButton("Exit");
    private File audioFile = new File("src\\action.wav");

    MenuClass() {
        frame.setSize(626, 417);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image using an ImageIcon
        ImageIcon backgroundImage = new ImageIcon("src/gamemenu.jpg");
        ImageIcon cursorImage = new ImageIcon("src/cursor.png");
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage.getImage(),
                new Point(0, 0), // Hotspot at the top-left corner of the image
                "customCursor"
        );
        frame.setCursor(customCursor);

        // Create a JLabel to display the background image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 626, 417);

        frame.add(backgroundLabel);
        frame.add(this);

        startButton.setFont(new Font("Ink free", Font.BOLD, 25));
        exitButton.setFont(new Font("Ink free", Font.BOLD, 25));

        // Adjust the position of the buttons
        startButton.setBounds(250, 130, 100, 50);
        exitButton.setBounds(250, 230, 100, 50);

        // Remove button borders
        startButton.setBorderPainted(false);
        exitButton.setBorderPainted(false);

        // Remove button focus painting
        startButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        // Customize button background colors
        startButton.setBackground(Color.YELLOW); // Background color on normal state
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(Color.ORANGE); // Background color on hover
                playSound(audioFile);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(Color.YELLOW); // Background color on normal state
            }
        });

        exitButton.setBackground(Color.RED); // Background color on normal state
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(Color.PINK); // Background color on hover
                playSound(audioFile);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(Color.RED); // Background color on normal state
            }
        });

        frame.add(startButton);
        frame.add(exitButton);

        startButton.addActionListener(this);
        exitButton.addActionListener(this);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            try {
                Car_shape obj = new Car_shape();
                obj.shapeed();
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        } else if (e.getSource() == exitButton) {
            int option = JOptionPane.showConfirmDialog(frame, "Do you really want to quit the game?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // Method to play audio from a file
    private void playSound(File file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

}
