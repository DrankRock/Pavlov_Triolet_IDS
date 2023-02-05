package _pkg_echo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class EchoThread extends Thread {
	private Socket clientSocket;
	private int clientID;
	private Communication_Server server;
	public EchoThread(Socket s, int id, Communication_Server server) {
		clientSocket = s;
		clientID = id;
		this.server = server;
	}
	
	public String getList() {
		Map<Integer, Socket> list = server.getList();
		String out = "";
		for (Map.Entry<Integer, Socket> entry : list.entrySet()) {
			out+=entry.getKey()+", ";
		}
		return out;
	}
	
	public void run() {
		try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine;
			out.println(clientID); // Initialize client id
			while ((inputLine = in.readLine()) != null) { // action loop
				switch(inputLine) {
					case "b" :
						String message = in.readLine();
						System.out.println("Client "+clientID+" trying to broadcast : "+message);
						server.broadcast(message, clientID);
						break;
					case "l" :
						out.println(this.getList());
						break;
					case "m":
						break;
				}
				System.out.println("Client "+clientID+" says : "+inputLine);
				//out.println(inputLine);
			}
		} catch (Exception e) {
			System.out.println(
					"Exception caught when trying to communicate with client ");
			System.out.println(e.getMessage());
		}
	}	
}
