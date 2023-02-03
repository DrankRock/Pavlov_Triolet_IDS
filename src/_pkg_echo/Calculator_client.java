package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Calculator_client extends Client{
	private char EOT = 0x04;
	private String EOT_s = String.valueOf(EOT);

	public void actionLoop(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
		String userInput;
        int iterator = 0;
        String toPrint;
        do {
        	switch(iterator % 3) {
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
        	userInput = stdIn.readLine();
        	out.println(""+iterator%3+"|"+userInput);
            if (iterator%3 == 2) {
            	String result = in.readLine();
            	if (result != null) {
                	if (result.equals(EOT_s)) {
                		System.out.println("Server closed, closing connection ...");
                		System.exit(1);
                	}
                	System.out.println("Result : "+result);
            	} else {
            		throw new IllegalArgumentException("An error occured resulting in result being null.\nClosing Client..");
            	}
            }
            iterator ++;
        }while (userInput != null);
	}
	
	
	public static void main(String[] args) {
        Calculator_client client = new Calculator_client();
        client.startClient(args);
        
		
	}

}
