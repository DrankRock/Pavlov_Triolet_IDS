import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.plaf.InsetsUIResource;

/**
 * This class was created using WindowsBuilder in Eclipse, then adapted to suit our needs.
 */
public class ConnexionClient {

	private JFrame frame;
	private JPasswordField pwdPassword;
	private JTextField txtUsername;
    private ClientImpl ci;

	/**
	 * Create the application.
	 */
	public ConnexionClient(ClientImpl ci) {
        this.ci = ci;
		initialize();
        frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 511, 710);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(3, 1));
		
		JPanel user = new JPanel();
		user.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));
		panel.add(user);
		user.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
		user.add(lblUsername, BorderLayout.NORTH);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Nimbus Sans", Font.BOLD, 40));
		user.add(txtUsername, BorderLayout.CENTER);
		txtUsername.setColumns(10);
        txtUsername.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.GRAY));
        txtUsername.setMargin(new InsetsUIResource(0, 20, 0, 20));
		
		JPanel pass = new JPanel();
		pass.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));
		panel.add(pass);
		pass.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
		pass.add(lblPassword, BorderLayout.NORTH);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("");
		pwdPassword.setFont(new Font("Nimbus Sans", Font.BOLD, 40));
        pwdPassword.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.GRAY));
        pwdPassword.addKeyListener(new KeyListener(){
                @Override
                public void keyPressed(KeyEvent e) {
                    // do nothing
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // connect
                    if (e.getKeyCode() == KeyEvent.VK_ENTER){
                        connect();
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    // do nothing
                }
        });
		pass.add(pwdPassword, BorderLayout.CENTER);
		
		JPanel login = new JPanel();
		panel.add(login);
		login.setLayout(new BorderLayout(0, 0));
		login.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont( new Font("Nimbus Sans", Font.BOLD, 60));
		btnLogin.setBackground(new Color(192, 245, 0));
		btnLogin.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.GRAY));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                connect();
			}
		});
        
		login.add(btnLogin, BorderLayout.CENTER);
	}

    private void connect(){
        String username = txtUsername.getText();
        String password = new String(pwdPassword.getPassword());
        ci.connect(username, password);
        frame.dispose();
        System.exit(0);
    }

}