package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The Class Calculator_client extends the Client class
 * to create a client with the sole purpose of sending
 * the server operations for it to complete.
 */
public class Calculator_client extends Client{
	private String EOT_s = String.valueOf(0x04); // to manage the closing of the server or the client
	
	/**
	 * The action loop method that handles the communication
	 * between the client and server.
	 * 
	 * @param in - BufferedReader to read input from the server
	 * @param stdIn - BufferedReader to read input from the user
	 * @param out - PrintWriter to send output to the server
	 * @throws IOException - If an input/output error occurs
	 */
	public void actionLoop(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
		String userInput;
        int iterator = 0;
        String toPrint;
        do {
        	switch(iterator % 3) { // only three possible case, so iterator doesn't have to be anything else
            	case 0:
            		toPrint = "Int 1: ";
            		break;
            	case 1:
            		toPrint = "Operation (+-/*) : ";
            		break;
            	default:
            		toPrint = "Int 2: ";
            		break;
        	}
        	System.out.print(toPrint);
        	userInput = stdIn.readLine(); // read input from the user, expecting it to be what is asked for
        	out.println(userInput);
            if (iterator%3 == 2) { // if the second integer has been entered
            	String result = this.process(in.readLine()); // get the result from the server
                System.out.println("Result : "+result);
            }
            iterator ++;
        }while (userInput != null);
	}
	
	/**
	 * Main method to start the client.
	 **/
	public static void main(String[] args) {
        Calculator_client client = new Calculator_client();
        client.startClient(args);
        
		
	}

}
