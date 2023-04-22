/**
 * @author Matvei Pavlov
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Gui class to help visualise the ping pong behavior
 *
 * @author Matvei Pavlov
 * @author Hugo Triolet
 */
public class Gui extends JFrame {

    private Color baseColor;

    private final Controller controller;
    private Color currentColor;
    JButton changeColorButton;
    private int currentPings;
    private int currentPongs;
    private JLabel pingLabel;
    private JLabel pongLabel;

    /**
     *
     * @param thisController
     * @param mode
     */
    public Gui(Controller thisController, String mode) {
        this.controller = thisController;
        this.currentColor = Color.blue;
        setTitle(mode);
        setSize(400, 400);
        this.setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        baseColor = Color.GRAY;

        getContentPane().setBackground(baseColor);

        JButton pingButton = new JButton("PING");
        changeColorButton = new JButton("choose color");
        changeColorButton.addActionListener(e -> this.choseColor());
        changeColorButton.setBackground(currentColor);
        changeColorButton.setForeground(new Color(255-currentColor.getRed(), 255-currentColor.getGreen(),
                255-currentColor.getBlue()));

        // -- Set the specific instructions if the mode is producer or consumer
        if (mode.equals("Consumer")){
            pingButton.setEnabled(false);
            pingButton.setVisible(false);
            changeColorButton.setEnabled(false);
            changeColorButton.setVisible(false);
            currentPings = 0;
            pingLabel = new JLabel("<html>Pings :<br>0</html>");
            // Set the horizontal and vertical alignment to center
            pingLabel.setHorizontalAlignment(JLabel.CENTER);
            pingLabel.setVerticalAlignment(JLabel.CENTER);
            Font font = pingLabel.getFont().deriveFont(30f);
            pingLabel.setFont(font);
            pongLabel = new JLabel("");
        } else {
            pingLabel = new JLabel("");
            currentPongs = 0;
            pongLabel = new JLabel("<html>Pongs :<br>0</html>");
            pongLabel.setHorizontalAlignment(JLabel.CENTER);
            pongLabel.setVerticalAlignment(JLabel.CENTER);
            Font font = pongLabel.getFont().deriveFont(30f);
            pongLabel.setFont(font);
        }



        // -- Setup the correct gridlayout dpeending on the mode
        getContentPane().setLayout(new GridLayout(0, 3));
        getContentPane().add(new JLabel(""));
        if (mode.equals("Consumer")){
            getContentPane().add(new JLabel(""));
        } else {
            getContentPane().add(changeColorButton);
        }
        getContentPane().add(new JLabel(""));

        getContentPane().add(new JLabel(""));
        if (mode.equals("Consumer")){
            getContentPane().add(new JLabel(""));
        } else {
            getContentPane().add(pingButton);
        }
        getContentPane().add(new JLabel(""));

        if (mode.equals("Consumer")){
            getContentPane().add(pingLabel);
        } else {
            getContentPane().add(new JLabel(""));
        }
        getContentPane().add(new JLabel(""));
        if (mode.equals("Consumer")){
            getContentPane().add(new JLabel(""));
        } else {
            getContentPane().add(pongLabel);
        }

        setVisible(true);
    }

    /**
     * Chose the color of the ping to send, and set the color of the button to this color,
     * Also modifies the color of the text to be the inverse of the background so that
     * the text is always readable
     */
    private void choseColor(){
        this.currentColor = JColorChooser.showDialog(this,"Select a color",this.currentColor);
        this.changeColorButton.setBackground(currentColor);
        this.changeColorButton.setForeground(new Color(255-currentColor.getRed(), 255-currentColor.getGreen(),
                255-currentColor.getBlue()));
    }

    /**
     * Change color as called by the consumer, increments ping count and changes color
     * @param c new color of the background
     */

    public void changeColor(Color c){
        this.currentColor = c;
        this.currentPings ++;
        this.flickerPing();
    }

    /**
     * Pong function called by Producer, calls flicker function
     */
    public void pong(){
        this.currentPongs += 1;
        this.flickerPong();
    }

    /**
     * Does a 200ms green background before changing to the ping color, to show the reception of a ping
     */
    public void flickerPing(){
        Thread anon = new Thread(() -> {
            String text = "<html>Pings rec:<br>"+this.currentPings+"</html>";
            getContentPane().setBackground(Color.green);
            this.pingLabel.setText(text);
            try {
                Thread  .sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getContentPane().setBackground(currentColor);
        });
        anon.start();
    }

    /**
     * Does a 200ms green background to show the reception of a pong
     */
    public void flickerPong(){
        Thread anon = new Thread(() -> {
            String text = "<html>Pongs rec:<br>"+this.currentPongs+"</html>";
            getContentPane().setBackground(Color.green);
            this.pongLabel.setText(text);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getContentPane().setBackground(baseColor);
        });
        anon.start();
    }

}