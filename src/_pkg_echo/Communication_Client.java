package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Communication_Client extends Client {
	int id = 0;

	public void actionLoop(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
		String userInput, serverInput;
		String id = in.readLine();
		this.id = Integer.valueOf(id);
		System.out.println("Given ID : "+this.id);
		System.out.println("# Instructions : ");
		System.out.println("# b <message> to broadcast,");
		System.out.println("# m <id> <message> to send message to specific client,");
		System.out.println("# l to list all clients");
        while ((userInput = stdIn.readLine()) != null) {
        	String[] inputSplit = userInput.split(" ");
        	out.println(inputSplit[0]);
        	switch (inputSplit[0]) {
        		case "b":
        			System.out.println("Broadcast time !");
        			String msg = "";
        			for(int i = 1; i<inputSplit.length; i++) {
        				msg+=inputSplit[i]+" ";
        			}
        			System.out.println("About to broadcast '"+msg+"'");
        			out.println(msg);
        			break;
        		case "m":
        			System.out.println("private message");
        			System.out.println("ID : "+inputSplit[1]);
        			System.out.println("Message : "+inputSplit[2]);
        			break;
        		case "l":
        			System.out.println("list !");
        			String list = this.process(in.readLine());
        			System.out.println(list);
        			break;
        	}
            out.println(userInput);
        }
	}
	
	public static void main(String[] args) {
		Communication_Client client = new Communication_Client();
		client.startClient(args);
	}

}
