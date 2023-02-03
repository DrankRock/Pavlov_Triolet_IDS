package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Calculator_client {
	static private char EOT = 0x04;
	static private String EOT_s = String.valueOf(EOT);

	public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        System.out.println("Client Starting on "+hostName+":"+portNumber);

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;
            int iterator = 0;
            String toPrint;
            do {
            	switch(iterator % 3) {
	            	case 0:
	            		toPrint = "Int 1: ";
	            		break;
	            	case 1:
	            		toPrint = "Operation: ";
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
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
        
		
	}
	
	static private void calculation(String line) {
		int d1, d2;
		String operand;
		String[] items = line.split(" ");
		d1 = Integer.valueOf(items[0]);
		d2 = Integer.valueOf(items[2]);
		operand = items[1];
		System.out.println(""+d1+" "+operand+" "+d2);
	}

}
