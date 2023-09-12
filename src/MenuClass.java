import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuClass extends JPanel implements ActionListener {

    private JFrame frame = new JFrame("Game Main Menu");
    private JButton startButton = new JButton("Start");
    private JButton exitButton = new JButton("Exit");

    MenuClass() {
        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image using an ImageIcon
        ImageIcon backgroundImage = new ImageIcon("src/menubackground.png");

        // Create a JLabel to display the background image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 600, 600);

        frame.add(backgroundLabel);
        frame.add(this);

        startButton.setFont(new Font("Arial", Font.BOLD, 25));


        // Adjust the position of the buttons
        startButton.setBounds(250, 300, 100, 50);
        exitButton.setFont(new Font("Arial", Font.BOLD, 25));
        exitButton.setBounds(250, 400, 100, 50);

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
                Car_shape obj=new Car_shape();
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
            System.exit(0);
        }
    }

}
