public class SecurityTest {
    public static void main(String[] args){
        byte[] salt = Security.getSalt();
        for (int i=0; i<salt.length; i++){
            Tools.dprint("["+i+"] - "+salt[i]);
        }
        String tohex = Security.bytesToHex(salt);
        Tools.dprint(tohex);
        byte[] tobyte = Security.hexToBytes(tohex);
        for (int i=0; i<tobyte.length; i++){
            Tools.dprint("["+i+"] - "+tobyte[i]);
        }
        String pass = "dCode";
        Tools.dprint(Security.encode(pass));

        Tools.dprint("Trying some good encode in sha 256 :");
        String username = "Matvei";
        Tools.dprint("Username : "+username);
        String my_salt = Security.bytesToHex(Security.getSalt());
        Tools.dprint("Salt : "+my_salt);
        String password = "NiceGuy67!";
        Tools.dprint("Password : "+password);
        Tools.dprint("Normal encode : "+Security.encode(password));
        Tools.dprint("Salted encode : "+Security.encode(password, my_salt));
    }
}