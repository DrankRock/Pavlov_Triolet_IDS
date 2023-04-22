import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;

public class View extends JFrame {
    private Color backgroundColor;

    private JButton leftButton;
    private JButton rightButton;
    private int number;
    private Controller thisController;
    private final int buttonWidth = 30;
    private JPanel numberPanel;

    public View(int number, Color backgroundColor, int totalWindows, Controller thisController) {
        this.number = number;
        this.backgroundColor = backgroundColor;

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


        // This whole class was copied-paste from oracle doc : https://docs.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html
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
}