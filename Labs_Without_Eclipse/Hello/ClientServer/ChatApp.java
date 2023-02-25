import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * GUI of a chat app client. The initial base swing elements were implemented by ChatGPT.
 * This is a View, it should not make operations by itself appart from gui actions. 
 */
public class ChatApp extends JFrame implements ActionListener {
    private ColoredTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Info_itf_Impl user;
    private String username;
    private Server communicationWithServer;
    Boolean shiftIsPressed = false;
    /**
     * Constructor of swing elements, and initialisation 
     * @param user the name of the user
     * @throws RemoteException when using Registry operations
     * @throws NotBoundException when using Registry operations
     */
    public ChatApp(Info_itf_Impl infos) throws RemoteException, NotBoundException {
        this.user = infos;
        this.username = infos.getName();
        Registry registry = LocateRegistry.getRegistry("localhost"); // get the registry of the host given in argument
		this.communicationWithServer = (Server) registry.lookup("RunningServerPT1");
        
        setTitle("Chat v0.1 - "+username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width = 700, height = 800; //this has to be fixed, for the coloredTextPane to correctly be set
        Dimension thisDim = new Dimension(width, height);
        this.setPreferredSize(thisDim);
        this.setMinimumSize(thisDim);

        // custom textPane to add colored text
        chatArea = new ColoredTextPane(new Dimension(width-100, height-100));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

		this.setBounds(100, 100, 511, 710);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.gray));
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel inputPanel = new JPanel();
		panel.add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setBorder(BorderFactory.createMatteBorder(20, 10, 20,10, Color.gray));
		inputPanel.setLayout(new BorderLayout(0, 0));
		
		inputField = new JTextField();
		inputPanel.add(inputField);
		inputField.setColumns(10);
		inputField.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
        inputField.addKeyListener(new KeyListener() {
            // Note : We check if shift is pressed because shift+Enter is supposed to create a \n, not send.
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftIsPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftIsPressed = false;
                    inputField.setText(inputField.getText()+"\n");
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !shiftIsPressed) {
                    send();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
            
        });
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBackground(new Color(127, 255, 0));
		btnNewButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
        btnNewButton.addActionListener(this);
		inputPanel.add(btnNewButton, BorderLayout.EAST);
		
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10));
        chatPanel.add(chatScrollPane);
		panel.add(chatPanel, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * On button click, send message to server
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        send();
    }

    private void send(){
        String inputText = inputField.getText();
        if (inputText.length() > 0) {
            try {
                communicationWithServer.message(user, inputText);
                inputField.setText("");
            } catch (RemoteException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), "Couldn't connect to server ...", "Dialog", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    /**
     * Add a message to the message board
     * @param user the sender
     * @param s the message
     */
    public synchronized void addMessage(String user,String s){
        if (this.username.equals(user))
            chatArea.normalMessage("You: " + s);
        else {
            chatArea.normalMessage(user+": " + s);
        }
    }
    /**
     * Add message with no user (server messages)
     * @param s
     */
    public synchronized void addMessage(String s){
        chatArea.normalMessage(s);
    }

    /**
     * Add message as an array of string (for loading history))
     * @param s
     */
    public void addMessages(ArrayList<String> s){
        for (String st : s){
            chatArea.normalMessage(st);
        }
    }

    /**
     * run function, to be used by caller class
     * @throws RemoteException
     */
    public void run() throws RemoteException{
        this.setVisible(true);
    }
    
    public String toString(){
        return "ChatApp v0.1";
    }

    public void setInfos(Info_itf_Impl inf){
        this.user = inf;
    }

    public void forceExit(){
        chatArea.errorMessage("The Server closed. This window will now stop functionning. Please exit and re-launch when the server is running again. Thank you for your trust.\nPlease note that any message sent will likely result in an error.");
    }
}