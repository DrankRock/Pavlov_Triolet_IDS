package com.clientserver;

import java.rmi.*;

public interface Info_itf extends Remote {
		public String getName() throws RemoteException;
		public int getID() throws RemoteException;
}