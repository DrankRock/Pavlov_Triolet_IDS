
import java.io.Serializable;
import java.rmi.*;

public class Info_itf_Impl implements Info_itf, Serializable{
	public String name;
	public int ID;
	public Info_itf_Impl(String s, int id) throws RemoteException{ 
		name = s;
		ID = id;
	}
	public String getName() throws RemoteException{
		return name;
	}
	public int getID() throws RemoteException{
		return ID;
	}
	public String toString(){
		return name+" - "+ID;
	}
}