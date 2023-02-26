/**
 * UserData class to manipulate easily user - salt - password
 */
public class UserData {
    private String user;
    private String salt;
    private String password;
    /**
     * Constructor of a UserData
     * @param user the username
     * @param salt the salt for the user's password
     * @param password the hashed and salted password for the user
     */
    public UserData(String user, String salt, String password){
        this.user = user;
        this.salt = salt;
        this.password = password;
    }
    /**
     * Get the username
     * @return the username
     */
    public String getUser() {
        return user;
    }
    /**
     * Set the username
     * @param user the username 
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * get the salt
     * @return the salt in a string of a hex
     */
    public String getSalt() {
        return salt;
    }
    /**
     * set the salt
     * @param salt a String containing byte[] as hex 
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
    /**
     * get the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * set the password
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    

}