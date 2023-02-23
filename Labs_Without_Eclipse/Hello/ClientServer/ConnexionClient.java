import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConnexionClient extends JDialog implements ActionListener {
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton acceptButton;
    private JButton closeButton;
    private String user;
    private String pass;
    ChatApp parent;

    public ConnexionClient(ChatApp parent) {
        super(parent, "Login", true);
        this.parent = parent;
        System.out.println(parent.toString());
        setLayout(new GridLayout(3, 2));

        // Create user label and text field
        JLabel userLabel = new JLabel("User:");
        userField = new JTextField();
        add(userLabel);
        add(userField);

        // Create password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        // Create accept and close buttons
        acceptButton = new JButton("Accept");
        acceptButton.addActionListener(this);
        acceptButton.setBackground(Color.GREEN);
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        closeButton.setBackground(Color.RED);
        add(acceptButton);
        add(closeButton);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == acceptButton) {
            // Handle accept button click
            
            user = userField.getText();
            pass = new String(passwordField.getPassword());
            parent.connect(user, pass);
            // Here, you can do the necessary authentication logic
            this.dispose();
        } else if (e.getSource() == closeButton) {
            // Handle close button click
            dispose();
        }
    }
}