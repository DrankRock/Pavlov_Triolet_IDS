
import javax.swing.*;

import java.awt.*;

/**
 * GUI of the server, to show the logs
 */
public class ServerApp extends JFrame{
    /**
     * chatArea containing all the logs displayed in the gui
     */
    private ColoredTextPane chatArea;

    /**
     * Constructor
     */
    public ServerApp() {
        int width = 1000, height = 800;
        setTitle("Server logs - v0.1 ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension thisDimension = new Dimension(width, height);
        this.setSize(thisDimension);
        this.setPreferredSize(thisDimension);
        this.setMinimumSize(thisDimension);
        chatArea = new ColoredTextPane(new Dimension(width-100, height-100));
        chatArea.switchNightMode();
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        Container contentPane = getContentPane();
        contentPane.add(chatScrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * add message to chatArea
     * @param s message to add
     */
    public void log(String s){
        chatArea.normalMessage(s);
    }

    /**
     * add message to chatArea
     * @param s message to add
     * @param size custom size
     */
    public void log(String s, int size){
        chatArea.normalMessage(s, size);
    }


    /**
     * print a cool ascii art of the server (＾▽＾)
     */
    public void AsciiStart(){
        this.log("██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████", 6);
        this.log("███░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░███░░░░░░░░███████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░███", 6);
        this.log("█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░░██░░░░░░░░███████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██", 6);
        this.log("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░░░█░░░░░░░░███████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█", 6);
        this.log("░░░░░░░█████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██░░░░░░█████░░░░░░░░░░░░░░░███████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████░░░░░░░", 6);
        this.log("░░░░░░░██████████████░░░░░░░███████░░░░░░██░░░░░░█████░░░░░░░█░░░░░░░███████████░░░░░░░███░░░░░░░███████░░░░░░██░░░░░░█████░░░░░░░", 6);
        this.log("░░░░░░░██████████████░░░░░░░███████████████░░░░░░█████░░░░░░░██░░░░░░░█████████░░░░░░░████░░░░░░░███████████████░░░░░░█████░░░░░░░", 6);
        this.log("█░░░░░░░░░███████████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░░████░░░░░░░███████░░░░░░░█████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░░█", 6);
        this.log("██░░░░░░░░░░░░░██████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░██████░░░░░░░█████░░░░░░░██████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░██", 6);
        this.log("████░░░░░░░░░░░░░████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░░██████░░░░░░░███░░░░░░░███████░░░░░░░░░░░░░░░░░█████░░░░░░░░░░░░░░░░░█", 6);
        this.log("███████░░░░░░░░░░░███░░░░░░░░░░░░░░░░░█████░░░░░░█████░░░░░░░██████░░░░░░░█░░░░░░░████████░░░░░░░░░░░░░░░░░█████░░░░░░█████░░░░░░░", 6);
        this.log("████████████░░░░░░░██░░░░░░░███████████████░░░░░░█████░░░░░░░███████░░░░░░░░░░░░░█████████░░░░░░░███████████████░░░░░░█████░░░░░░░", 6);
        this.log("████████████░░░░░░░██░░░░░░░███████░░░░░░██░░░░░░█████░░░░░░░████████░░░░░░░░░░░██████████░░░░░░░███████░░░░░░██░░░░░░█████░░░░░░░", 6);
        this.log("░░░░░░░█████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██░░░░░░█████░░░░░░░█████████░░░░░░░░░█████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████░░░░░░░", 6);
        this.log("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██░░░░░░█████░░░░░░░██████████░░░░░░░██████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████░░░░░░░", 6);
        this.log("░░░░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░░░░░░░░██░░░░░░█████░░░░░░░███████████░░░░░███████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████░░░░░░░", 6);
        this.log("█░░░░░░░░░░░░░░░███░░░░░░░░░░░░░░░░░░░░░░██░░░░░░█████░░░░░░░████████████░░░████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████░░░░░░", 6);
    }

}