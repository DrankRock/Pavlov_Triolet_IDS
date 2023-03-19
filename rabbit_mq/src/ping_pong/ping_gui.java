import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ping_gui extends JFrame {

    private JButton changeColorButton;

    public SimpleSwingGUI() {
        setTitle("Simple Swing GUI");
        setSize(400, 400);
        setLocationRelativeTo(null); // center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the button
        changeColorButton = new JButton("Change Color");
        changeColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor();
            }
        });

        // Add the button to the center of the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(changeColorButton, BorderLayout.CENTER);

        setVisible(true);
    }

    // Change the color of the whole window
    private void changeColor() {
        Color newColor = JColorChooser.showDialog(null, "Choose a color", getBackground());
        if (newColor != null) {
            getContentPane().setBackground(newColor);
        }
    }

    public static void main(String[] args) {
        new SimpleSwingGUI();
    }
}