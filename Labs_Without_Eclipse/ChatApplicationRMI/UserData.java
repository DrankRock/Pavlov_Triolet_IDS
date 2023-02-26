/**
 * UserData class to manipulate easily user - salt - password
 */
public class UserData {
    private String user;
    private String salt;
    private String password;
    public UserData(String user, String salt, String password){
        this.user = user;
        this.salt = salt;
        this.password = password;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    

}