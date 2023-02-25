
import java.io.Serializable;
import java.rmi.*;

public class Info_itf_Impl implements Info_itf, Serializable{
	private String name;
	private String ID;
	public Info_itf_Impl(String s, String id) throws RemoteException{ 
		name = s;
		ID = id;
	}
	public String getName() throws RemoteException{
		return name;
	}
	public String getID() throws RemoteException{
		return ID;
	}
	public String toString(){
		return name+" - "+ID;
	}
}