import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class GameOver extends JPanel {
    JFrame frame = new JFrame("Game Over");
    String time;
    int coinscore;
    JButton playagainbutton = new JButton("Play again");
    String userName;

    public GameOver() {
        setLayout(new BorderLayout()); // Use BorderLayout for the main panel
        add(playagainbutton, BorderLayout.SOUTH); // Add the button at the bottom

        playagainbutton.setPreferredSize(new Dimension(80, 30)); // Set button size
        playagainbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                   // frame.dispose();
                    Car_shape shape=new Car_shape();
                    shape.shapeed();
                   // frame.dispose();
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();


            }
        });


        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);
    }

    void overdetail(String formattedtime, int score) throws IOException {
        coinscore = score;
        time = formattedtime;

        SwingUtilities.invokeLater(() -> {

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(this); // Add the GameOver panel to the JFrame
            frame.pack(); // Adjust JFrame size to fit components
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
        String fileName = "Scores.txt";
        String newDataToAppend = time;
       // userName = JOptionPane.showInputDialog(this, "Please enter your name:");

        //TO WRITE IN FILE
        FileWriter filewriter = new FileWriter(fileName, true);
        filewriter.write(newDataToAppend +" "+userName+ "\n");
        filewriter.close();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.setFont(new Font("INK free", Font.BOLD, 75));
        String gameOverText = "Game Over";
        g.drawString(gameOverText, 10, 100);
        g.setFont(new Font("INK free", Font.BOLD, 30));
        g.drawString("    Time Taken = " + time, 20, 200);
        g.drawString("    Score = " + coinscore, 20, 300);
    }


}
