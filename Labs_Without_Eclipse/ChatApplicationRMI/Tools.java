import java.nio.charset.Charset;
import java.security.SecureRandom;

/**
 * Tools set used around
 */
public class Tools {
    public static boolean DEBUG = false;
    /**
     * Print only if DEBUG is true
     * @param s String to print
     */
    public static void dprint(String s){
        if (DEBUG){
            System.out.println(s);
        }
    }

    /**
     * Return a random String
     * It's a pretty unique String. Not absolutely unique though. This can't be achieved.
     * @param size of the array of bytes
     * @return random bytes array as a String
     */
    public static String randomString(int size){
        byte[] array = new byte[size];
        new SecureRandom().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
