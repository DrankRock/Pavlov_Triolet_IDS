import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Nathan Pruett
 * <a href="https://coderanch.com/t/333397/java/chnage-angle-button">...</a>
 */
class AngledButtonTester extends JFrame {
    public AngledButtonTester() {
        super( "AngledButton Tester" );
        AngledButton b = new AngledButton( "Angled" );
        b.addActionListener( new SpinTheButton() );
        JPanel p = new JPanel();
        JButton old = new JButton( "Normal" );
        p.add( b );
        p.add( old );
        p.setOpaque( false );
        getContentPane().add( p );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 300, 300 );
        setVisible( true );
    }

    private class SpinTheButton implements ActionListener, Runnable {
        private AngledButton b;

        public void actionPerformed( ActionEvent e ) {
            if( e.getSource() instanceof AngledButton ) {
                b = (AngledButton)e.getSource();
                Thread t = new Thread( this );
                t.start();
            }
        }

        public void run() {
            double d = b.getAngle();
            while( d < 2 * Math.PI ) {
                b.setAngle( d );
                d += 0.1;
                try {
                    Thread.sleep( 80 );
                }
                catch( InterruptedException x ) {
                    System.err.println( "Spinning interrupted!" );
                }
            }
            b.setAngle( 0.0 );
        }
    }
}