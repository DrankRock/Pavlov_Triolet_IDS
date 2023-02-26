import java.awt.*;

import javax.swing.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * This whole function was initialy found here : https://stackoverflow.com/a/9652143
 * And modified to suit our needs.
 * 
 * ColoredTextPane is a JPanel containing a JTextPane, that is not editable, and adds text to the chat.
 * The coloredtextpane is actually editable sometimes, for a short timespan, to add messages.
 */
public class ColoredTextPane extends JPanel
{
    Color[] colorSet; // current color set
    // Text, Error, valid, whisper, background
    // Whisper is never used because we did not have the time to implement private messages.
    Color[] lightColors = {Color.BLACK, Color.RED, Color.GREEN, Color.CYAN, Color.WHITE};
    Color[] nightColors = {Color.GREEN, Color.RED, Color.YELLOW, Color.PINK, Color.BLACK};
    //night mode is not currently setable.
    private JTextPane tPane;
    private boolean lightMode;
    private JPanel insideBorder;

    /**
     * Constructor 
     * @param d the dimensions of the pane
     */
    public ColoredTextPane(Dimension d){
        super(new BorderLayout());
        insideBorder = new JPanel(new BorderLayout());
        insideBorder.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        colorSet = lightColors; //light mode by default
        lightMode = true;

        tPane = new JTextPane();                
        tPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tPane.setMargin(new Insets(5, 5, 5, 5));
        this.setBackground(colorSet[4]);
        tPane.setEditable(false);
        tPane.setFont(new Font("Nimbus Sans", Font.BOLD, 18));

        tPane.setMinimumSize(d);
        insideBorder.add(tPane);
        this.add(insideBorder);

        setVisible(true);   
    }

    /**
     * Because dimensions are not always obvious
     * @param w width
     * @param h height
     */
    public ColoredTextPane(int w, int h){
        this(new Dimension(w, h));    
    }

    /**
     * Append a String to the textPane. Synchronized to avoid problems with setEditable, like
     * the tpane staying editable
     * @param tp the JTextPane
     * @param msg the Message to add
     * @param c the color of the message
     */
    private synchronized void appendToPane(JTextPane tp, String msg, Color c)
    {
        // This function is the one completely copied from stackoverflow. Sorry, we really
        // wanted colored text ＼(≧▽≦)／
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Nimbus Sans");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 18);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tPane.setEditable(true);
        tp.replaceSelection(msg);
        tPane.setEditable(false);
    }

    /**
     * Append a String to the textPane. Synchronized to avoid problems with setEditable, like
     * the tpane staying editable. Size is editable here.
     * @param tp the JTextPane
     * @param msg the Message to add
     * @param c the color of the message
     * @param size the size of the text
     */
    private synchronized void appendToPane(JTextPane tp, String msg, Color c, int size)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, size);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tPane.setEditable(true);
        tp.replaceSelection(msg);
        tPane.setEditable(false);
    }

    /**
     * Add message with normal text color
     * @param s the message to print
     */
    public void normalMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[0]);
    }

    /**
     * Add message with normal text color, but custom size
     * @param s the message to print
     * @param size the size of the text
     */
    public void normalMessage(String s, int size){
        appendToPane(tPane, s+"\n", colorSet[0], size);
    }

    /**
     * Add message with error text color
     * @param s the message to print
     */
    public void errorMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[1]);
    }

    /**
     * Add message with valid text color
     * @param s the message to print
     */
    public void validMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[2]);
    }

    /**
     * Add message with whisper (private message) text color
     * @param s the message to print
     */
    public void whisperMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[3]);
    }

    /**
     * Switch the theme to light mode. 
     * 
     * *NOTE* This will not work if not used in the beginning, 
     * because we don't modify previous text color
     */
    public void switchLightMode(){
        if (!lightMode){
            colorSet = lightColors;
            tPane.setBackground(colorSet[4]);
            lightMode = true;
        }
    }

    /**
     * Switch the theme to night mode. 
     * 
     * *NOTE* This will not work if not used in the beginning, 
     * because we don't modify previous text color
     */
    public void switchNightMode(){
        if (lightMode){
            colorSet = nightColors;
            tPane.setBackground(colorSet[4]);
            lightMode = false;
        }
    }

}