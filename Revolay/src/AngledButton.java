import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Nathan Pruett
 * <a href="https://coderanch.com/t/333397/java/chnage-angle-button">...</a>
 */
public class AngledButton extends JButton {

    protected double angle = 0D;

    public AngledButton() {
        this( null, null );
    }

    public AngledButton( Action a ) {
        this();
        setAction( a );
    }

    public AngledButton( Icon icon ) {
        this( null, icon );
    }

    public AngledButton( String text ) {
        this( text, null );
    }

    public AngledButton( String text, Icon icon ) {
        super( text, icon );
    }

    public void paint( Graphics g ) {

        Point oldCenter = new Point( super.getPreferredSize().width /
                2, super.getPreferredSize().height / 2 );
        Point bigCenter = new Point( getLargest() / 2, getLargest() /
                2 );
        Point newCenter = new Point( bigCenter.x - oldCenter.x,
                bigCenter.y - oldCenter.y );

        Image oldImage = createImage( getLargest(), getLargest() );
        Graphics oldG = oldImage.getGraphics();
        oldG.setClip( newCenter.x, newCenter.y,
                super.getPreferredSize().width,
                super.getPreferredSize().height );

        Image newImage = createImage( getLargest(), getLargest() );
        Graphics newG = newImage.getGraphics();

        super.paint( oldG );

        newG.drawImage( oldImage, 0, 0, this );

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor( getParent().getBackground() );
        g2.fillRect( 0, 0, getLargest(), getLargest() );
        g2.rotate( angle, getPreferredSize().width / 2,
                getPreferredSize().height / 2 );
        g2.drawImage( newImage, 0, 0, this );
    }

    protected void paintBorder( Graphics g ) {
        if( isBorderPainted() ) {
            Border border = getBorder();
            if( border != null ) {
                Point oldCenter = new Point(
                        super.getPreferredSize().width / 2,
                        super.getPreferredSize().height / 2 );
                Point bigCenter = new Point( getLargest() / 2,
                        getLargest() / 2 );
                Point newCenter = new Point(
                        bigCenter.x - oldCenter.x,
                        bigCenter.y - oldCenter.y );
                border.paintBorder( this, g, newCenter.x,
                        newCenter.y, super.getPreferredSize().width,
                        super.getPreferredSize().height );
            }
        }
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle( double angle ) {
        this.angle = angle;
        repaint();
    }

    public Dimension getPreferredSize() {
        Dimension d = new Dimension( getLargest(), getLargest() );
        return d;
    }

    protected int getLargest() {
        int w = super.getPreferredSize().width;
        int h = super.getPreferredSize().height;

        if( w > h ) {
            return w;
        }
        else {
            return h;
        }
    }

    public static void main( String[] arg ) {
        new AngledButtonTester();
    }
}