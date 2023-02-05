package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Communication_Client extends Client {
	int id = 0;
	
	public void startListening(BufferedReader in) {
		Thread backgroundThread = new Thread(new Runnable() {
		    public void run() {
		    	String servMess;
		    	try {
					while ((servMess = in.readLine()) != null) {
						System.out.println(servMess);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error encountered with the server, closing connection");
					System.exit(1);
				}
		    }
		});
		backgroundThread.start();
	}

	public void actionLoop(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
		String userInput, serverInput;
		String id = in.readLine();
		this.id = Integer.valueOf(id);
		this.startListening(in);
		System.out.println("Given ID : "+this.id);
		System.out.println("# Instructions : ");
		System.out.println("# b <message> to broadcast,");
		System.out.println("# m <id> <message> to send message to specific client,");
		System.out.println("# l to list all clients");
		System.out.println("# q to quit");
        while ((userInput = stdIn.readLine()) != null) {
        	String[] inputSplit = userInput.split(" ");
        	out.println(inputSplit[0]);
        	switch (inputSplit[0]) {
        		case "b":
        			String msg = "";
        			for(int i = 1; i<inputSplit.length; i++) {
        				msg+=inputSplit[i]+" ";
        			}
        			out.println(msg);
        			break;
        		case "m":
        			String msg2 = "";
        			out.println(inputSplit[1]);
        			for(int i = 2; i<inputSplit.length; i++) {
        				msg2+=inputSplit[i]+" ";
        			}
        			out.println(msg2);
        			break;
        		case "q":
        			System.out.println("Connexion closed.");
        			System.exit(1);
        		case "l":
        		default :
        			break;
        	}
        }
	}
	
	public static void main(String[] args) {
		Communication_Client client = new Communication_Client();
		client.startClient(args);
	}

}
