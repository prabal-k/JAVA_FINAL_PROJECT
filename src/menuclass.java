import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;

public class menuclass implements ActionListener {

    JFrame frame= new JFrame("Game Main Menu");
    JButton startbutton =new JButton();

    JButton exitbutton = new JButton();

    menuclass()
    {
        frame.setSize(600,600);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //start button

        startbutton.setBounds(280,100,100,50);
        startbutton.setForeground(Color.GREEN);
        startbutton.setFont(new Font("Arial", Font.BOLD, 25));
        startbutton.setBackground(Color.GREEN);
        startbutton.setText("Start");
        startbutton.setForeground(Color.WHITE);
        startbutton.setFocusPainted(false);

        //exit button
        exitbutton.setBounds(280,200,100,50);
        exitbutton.setForeground(Color.GREEN);
        exitbutton.setFont(new Font("Arial", Font.BOLD, 25));
        startbutton.setBackground(Color.GREEN);
        exitbutton.setText("Exit");
        startbutton.setForeground(Color.WHITE);



        exitbutton.setText("Exit");
        frame.add(startbutton);
        frame.add(exitbutton);

        startbutton.addActionListener(this);

        // Add a window listener to handle the close operation



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startbutton)
        {
            Car_shape carShape= null;
            try {
                carShape = new Car_shape();
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            carShape.shapeed();
            frame.dispose();
        }
        else if (e.getSource() == exitbutton) {
            System.exit(0);


        }

    }

}
