package _pkg_echo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Communication_Server {
	private int lastID = 0;
	private Map<Integer, Socket> socketMap = new HashMap<Integer, Socket>();
	
	
	public int nextID() {
		return lastID++;
	}
	
	public void broadcast(String message, int clientID) throws IOException {
		for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
			PrintWriter out = new PrintWriter(entry.getValue().getOutputStream(), true);
			out.println("(Global) "+clientID+" : \""+message+"\"");
		}
	}
	
	public void sendMessage(int senderID, int destID, String message) {
		try {
			if(socketMap.containsKey(destID)){
				PrintWriter out = new PrintWriter(socketMap.get(destID).getOutputStream(), true);
				out.println("(Private) "+senderID+" : \""+message+"\"");
			} else {
				PrintWriter out = new PrintWriter(socketMap.get(senderID).getOutputStream(), true);
				out.println("ERROR : ["+destID+"] couldn't be found");
			}
			
		} catch (IOException e) {
			System.out.println("Error found while sending message, can't find "+destID);
		}
	}
	
	public void removefromList(int id) {
		socketMap.remove(id);
	}
	
	public Socket getSocket(int id) {
		return socketMap.get(id);
	}
	
	public Map<Integer, Socket> getList() {
		return socketMap;
	}
	
	public void startServer(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}
		int portNumber = Integer.parseInt(args[0]);
		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));) {
			System.out.println("Starting Serveur multi-threaded on port "+portNumber);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				int id = nextID();
				socketMap.put(id, clientSocket);
				EchoThread et = new EchoThread(clientSocket, id, this);
				et.start();
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
			+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException {
		Communication_Server server = new Communication_Server();
		server.startServer(args);
	}
}
