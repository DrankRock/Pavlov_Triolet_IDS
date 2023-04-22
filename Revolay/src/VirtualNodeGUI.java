import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;

/**
 * Graphical User Interface of a Virtual node. The nodes are around an invisible circle to form a ring.
 * The windows are perfect circles. Buttons on the right and left are used to send pings to respectively the next and
 * precedent ball.
 * Note that precedent and next is about the number on the ball, not the litteral right or left.
 * @author Matvei Pavlov
 */
public class VirtualNodeGUI extends JFrame {
    /**
     * The background color
     */
    private Color backgroundColor;
    /**
     * The negative color of the background color to see the flicker whatever the color
     */
    private Color flickerColor;
    /**
     * Color of the text when flickering, if the negative color is dark
     */
    private Color flickerTextcolor;
    /**
     * The button to send a ping to the previous node
     */
    private JButton leftButton;
    /**
     * The button to send a ping to the next node
     */
    private JButton rightButton;
    /**
     * This node's number
     */
    private int number;
    /**
     * The caller of this class
     */
    private Controller thisController;
    /**
     * A subjectively good button width
     */
    private final int buttonWidth = 30;
    /**
     * The panel containing the number of the node that is supposed to suit the size of the panel, but doens't work
     * super well because of the circle
     */
    private JPanel numberPanel;

    /**
     * Constructor of the GUI of a virtual node. The current node's position depends of its value and of the
     * total number of windows that will be opened. The guis are opened around an imaginary circle.
     * @param number the value of the node
     * @param backgroundColor the background color of the node, randomly generated
     * @param totalWindows the total number of nodes
     * @param thisController the controller who called this GUI
     */
    public VirtualNodeGUI(int number, Color backgroundColor, int totalWindows, Controller thisController) {
        this.number = number;
        this.backgroundColor = backgroundColor;
        this.flickerColor = Utils.getNegativeColor(backgroundColor);
        if (Utils.isBright(flickerColor)){
            this.flickerTextcolor = Color.WHITE;
        } else {
            this.flickerTextcolor = Color.BLACK;
        }
        this.thisController = thisController;

        setTitle("Node " + number);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);


        numberPanel = new NumberPanel(number, backgroundColor);
        add(numberPanel, BorderLayout.CENTER);

        Font btnFont = new Font("Arial", Font.BOLD, 5);

        leftButton = new JButton("-");
        leftButton.setFont(btnFont);
        leftButton.setBackground(backgroundColor.brighter());
        leftButton.setPreferredSize(new Dimension(buttonWidth, 0));
        leftButton.addActionListener(e -> thisController.sendLeft());
        add(leftButton, BorderLayout.WEST);

        rightButton = new JButton("+");
        rightButton.setFont(btnFont);
        rightButton.setBackground(backgroundColor.darker());
        rightButton.setPreferredSize(new Dimension(buttonWidth, 0));
        rightButton.addActionListener(e -> thisController.sendRight());
        add(rightButton, BorderLayout.EAST);


        setSize(200, 200);
        setLocationRelativeTo(null);


        // This whole class was initially copied-paste from oracle doc :
        // https://docs.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html
        // It is best practice to set the window's shape in
        // the componentResized method.  Then, if the window
        // changes size, the shape will be correctly recalculated.
        addComponentListener(new ComponentAdapter() {
            // Give the window an elliptical shape.
            // If the window is resized, the shape is recalculated here.
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            }
        });

        setUndecorated(true);

        Rectangle bounds = getBounds();
        int windowSize = Math.max(bounds.width, bounds.height); // bounds, not size, to include the header
        int radius = (int) (windowSize / (2 * Math.sin(Math.PI / totalWindows))); // radius of the circle
        // the idea is to put all the windows around an imaginary circle

        double angle = 2 * Math.PI / totalWindows; // each window has a specific position around the circle depending
        // of its number
        int centerCircleX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        int centerCircleY = Toolkit.getDefaultToolkit().getScreenSize().height / 2; // center of the circle
        setLocation(
                (int) (centerCircleX + radius * Math.cos(angle * number)) - windowSize / 2,
                (int) (centerCircleY + radius * Math.sin(angle * number)) - windowSize / 2
        );
        setVisible(true);
    }


    /**
     * NumberPanel Class, containing a number that fits in all the available size.
     * This class was realized with the help of chatGPT, then modified during the evolution of the project.
     */
    private class NumberPanel extends JPanel {
        private int number;
        private Color backgroundColor;
        private Color textColor;

        /**
         * Constructor of the NumberPanel
         * @param number the node's number
         * @param backgroundColor the background color of the panel
         */
        public NumberPanel(int number, Color backgroundColor) {
            this.number = number;
            this.backgroundColor = backgroundColor;
            if (Utils.isBright(backgroundColor)) {
                textColor = Color.BLACK;
            } else {
                textColor = Color.WHITE;
            }
            setOpaque(true);
            setBackground(backgroundColor);
        }

        /**
         * Specific paintComponenet to have a number fit inside the circle.
         * Note that this doesn't always work, because of it being a circle.
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Font numberFont = new Font("Arial", Font.BOLD, getHeight() - 20);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(numberFont);
            g2d.setColor(textColor);
            String numberString = String.valueOf(number);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(numberString);
            int stringHeight = fontMetrics.getAscent();
            g2d.drawString(numberString, (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2);
        }
    }

    /**
     * Make the background color change to the negative of the background for 400ms on reception of a message.
     */
    public void flicker() {
        Thread anon = new Thread(() -> {
            this.numberPanel.setBackground(this.flickerColor);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.numberPanel.setBackground(backgroundColor);
        });
        anon.start();
    }
}