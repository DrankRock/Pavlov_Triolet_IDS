import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * The aim is to manipulate only strings, and make them byte[] only for the encoding
 */
public final class Security {
    /**
     * Simple unsalted SHA-256 hashing of a string
     * @param base the string to hash
     * @return the hashed string
     */
    public static String encode(String base){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Encoding impossible, SHA-256 doesn't exist");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Salted hashing of string in SHA-256
     * @param base the string to hash
     * @param salt the salt in hex
     * @return the hashed salted string
     */
    public static String encode(String base, String salt){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(Security.hexToBytes(salt));
            byte[] encodedhash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Encoding impossible, SHA-256 doesn't exist");
            e.printStackTrace();
        }
        return null;
    }
    public static String encode(UserData ud){
        return encode(ud.getPassword(), ud.getSalt());
    }

    /**
     * Convert a string composed of 
     * user:salt in hex:hashed password
     * to a UserData (String, String, String)
     * @param s
     * @return
     */
    public static UserData stringToUserdata(String s ){
        String[] spl = s.split(":");
        if (spl.length != 3){
            return null;
        } else {
            return new UserData(spl[0], spl[1] , spl[2]);
        }
    }

    /**
     * Function from : https://www.baeldung.com/sha-256-hashing-java
     * used to convert bytes array to hex representation in string
     * @param hash
     * @return
     */
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Convert a String containinh hexadecimal representation of bytes into a byte array
     * @param hash the hexadecimal representation
     * @return the corresponding byte array
     */
    public static byte[] hexToBytes(String hash){
        byte[] bytes = new byte[hash.length()/2];
        if(hash.length()%2 == 1){
            return null;
        }
        String current = "";
        for(int i=0; i<hash.length(); i++){
            current += hash.charAt(i);
            if (i%2 == 1){
                bytes[i/2] = (byte) Integer.parseInt(current, 16);
                current = "";
            }
        }
        return bytes;
    }
    /**
     * Get a random salt in a byte array
     */
    public static byte[] getSalt(){
        SecureRandom random = new SecureRandom(); // less predictable than Random
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}