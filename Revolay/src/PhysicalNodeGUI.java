import javax.swing.*;
import java.awt.*;

/**
 * The physical node gui, being a Simple rectangular window with the number of the node
 * and a button to chose which node to send a message. The message is a simple Ping.
 *
 * @author Matvei Pavlov
 */
public class PhysicalNodeGUI extends JFrame {
    /**
     * This is the back panel containing a color and a number that fits it.
     */
    private NumberPanel numberPanel;
    /**
     * This is the background color of the panel
     */
    private Color backgroundColor;
    /**
     * This is the node's value.
     */
    private int value;
    /**
     * This is the total number of nodes.
     */
    private int numberOfElements;
    /**
     * This is the caller class of the gui.
     */
    private NodeRunner nodeLauncher;

    /**
     * Constructor of the physical node
     * @param c the Color of the background (always Gray)
     * @param value the value of the node
     * @param numberOfElements the total number of nodes
     * @param nodeLauncher the caller of this class, a NodeRunner class
     */
    public PhysicalNodeGUI(Color c, int value, int numberOfElements, NodeRunner nodeLauncher){
        this.backgroundColor = c;
        this.value = value;
        this.numberOfElements = numberOfElements;
        this.nodeLauncher = nodeLauncher;
        String[] nodes = new String[numberOfElements-1];
        int cont = 0;
        for (int i = 0; i<numberOfElements; i++){
            if (i != value){
                nodes[cont] = String.valueOf(i);
                cont ++ ;
            }
        }
        JComboBox<String> destComboBox = new JComboBox<>(nodes);
        destComboBox.setPreferredSize(new Dimension(30, 0));
        setTitle("Physical Node " + value);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        numberPanel = new NumberPanel(value, c);
        add(numberPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(0, 2));
        bottomPanel.add(destComboBox);
        JButton send = new JButton("Send");
        send.addActionListener(e -> {
            ping(Integer.parseInt(destComboBox.getSelectedItem().toString()));
        });
        bottomPanel.add(send);

        this.add(bottomPanel, BorderLayout.SOUTH);

        setSize(200, 200);
        setLocationRelativeTo(null);

        Rectangle bounds = getBounds();
        int windowSize = Math.max(bounds.width, bounds.height); // bounds, not size, to include the header
        int radius = (int) (windowSize / (2 * Math.sin(Math.PI / numberOfElements))); // radius of the circle
        // the idea is to put all the windows around an imaginary circle

        double angle = 2 * Math.PI / numberOfElements; // each window has a specific position around the circle depending
        // of its number
        int centerCircleX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        int centerCircleY = Toolkit.getDefaultToolkit().getScreenSize().height / 2; // center of the circle
        setLocation(
                (int) (centerCircleX + radius * Math.cos(angle * value)) - windowSize / 2,
                (int) (centerCircleY + radius * Math.sin(angle * value)) - windowSize / 2
        );
        setVisible(true);
    }

    /**
     * NumberPanel Class, containing a number that fits in all the available size
     */
    private class NumberPanel extends JPanel {
        private int number;
        private Color backgroundColor;

        public NumberPanel(int number, Color backgroundColor) {
            this.number = number;
            this.backgroundColor = backgroundColor;
            setOpaque(true);
            setBackground(backgroundColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Font numberFont = new Font("Arial", Font.BOLD, getHeight() - 20);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(numberFont);
            if (Utils.isBright(backgroundColor)) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(new Color(255, 250, 250));
            }
            String numberString = String.valueOf(number);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(numberString);
            int stringHeight = fontMetrics.getAscent();
            g2d.drawString(numberString, (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2);
        }
    }

    /**
     * Make the window flash a Green color, to indicate that it received a message.
     * This class is currently not used because the flickering is very unreliable.
     */
    @SuppressWarnings("unused")
    public void flicker() {
        Thread anon = new Thread(() -> {
            this.numberPanel.setBackground(Color.green);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.numberPanel.setBackground(backgroundColor);
        });
        anon.start();
    }

    /**
     * Called on click of a button, try to ping the virtual destination, sending this action
     * to the Physical node for it to execute this
     * @param dest the value of the destination
     */
    public void ping(int dest){
        nodeLauncher.ping(dest);
    }
}
