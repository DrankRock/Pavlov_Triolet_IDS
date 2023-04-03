import java.awt.*;

/**
 * @author Matvei Pavlov
 */
public final class Utils {
    static public double getLuminance(Color c){
        //  (0.2126*R + 0.7152*G + 0.0722*B)
        return 0.2125*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue();

    }

    static public boolean isBright(Color c){
        //  (0.2126*R + 0.7152*G + 0.0722*B)
        return getLuminance(c) > 127.5;
    }

    static public boolean isDark(Color c){
        //  (0.2126*R + 0.7152*G + 0.0722*B)
        return getLuminance(c) <= 127.5;
    }

}
