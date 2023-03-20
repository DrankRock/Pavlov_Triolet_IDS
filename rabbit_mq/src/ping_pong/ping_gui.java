import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ping_gui extends JFrame {

    private JButton changeColorButton;

    public ping_gui() {
        setTitle("Producer");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the button
        changeColorButton = new JButton("PING");
        changeColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendPing();
            }
        });
        changeColorButton.setSize(40, 40);
        changeColorButton.setPreferredSize(40, 40);
        changeColorButton.setMaximumSize(changeColorButton.getPreferredSize());

        // Add the button to the center of the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(changeColorButton, BorderLayout.CENTER);

        setVisible(true);
    }

    // Change the color of the whole window
    private void sendPing() {
        Color newColor = Color.green;
        if (newColor != null) {
            getContentPane().setBackground(newColor);
        }
    }

    public static void main(String[] args) {
        new ping_gui();
    }
}