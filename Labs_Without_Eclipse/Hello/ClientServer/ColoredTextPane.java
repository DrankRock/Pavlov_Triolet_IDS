import java.awt.*;

import javax.swing.*;

import javax.swing.border.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * This whole function was initialy found here : https://stackoverflow.com/a/9652143
 * And modified to suit our needs.
 * 
 * ColoredTextPane is a JPanel containing a JTextPane, that is not editable, and adds text to the chat.
 */
public class ColoredTextPane extends JPanel
{
    Color[] colorSet; // current color set
    // Text, Error, valid, whisper, background
    Color[] lightColors = {Color.BLACK, Color.RED, Color.GREEN, Color.CYAN, Color.WHITE};
    Color[] nightColors = {Color.GREEN, Color.RED, Color.YELLOW, Color.PINK, Color.BLACK};
    private JTextPane tPane;
    private boolean lightMode;

    public ColoredTextPane(Dimension d){
        super(new BorderLayout());
        colorSet = lightColors;
        lightMode = true;

        MatteBorder eb = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY);
        tPane = new JTextPane();                
        tPane.setBorder(eb);
        tPane.setMargin(new Insets(5, 5, 5, 5));
        this.setBackground(colorSet[4]);
        tPane.setEditable(false);

        this.setPreferredSize(d);
        tPane.setPreferredSize(d);
        this.add(tPane);

        setVisible(true);   
    }
    public ColoredTextPane(int x, int y){
        this(new Dimension(x, y));    
    }

    private synchronized void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tPane.setEditable(true);
        tp.replaceSelection(msg);
        tPane.setEditable(false);
    }

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

    public void normalMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[0]);
    }

    public void normalMessage(String s, int size){
        appendToPane(tPane, s+"\n", colorSet[0], size);
    }

    public void errorMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[1]);
    }

    public void validMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[2]);
    }

    public void whisperMessage(String s){
        appendToPane(tPane, s+"\n", colorSet[3]);
    }

    public void switchLightMode(){
        if (!lightMode){
            colorSet = lightColors;
            tPane.setBackground(colorSet[4]);
            lightMode = true;
        }
    }

    public void switchNightMode(){
        if (lightMode){
            colorSet = nightColors;
            tPane.setBackground(colorSet[4]);
            lightMode = false;
        }
    }

}