package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The Client abstract class generalizes the Client class
 * @author matvei
 *
 */
public abstract class Client {
	public String EOT_s = "-1";
	public BufferedReader in;
	
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
		// TODO Action loop
		String userInput;
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine());
        }
	}
	
	public String process(String s) {
		if (s != null) {
        	if (s.equals(EOT_s)) {
        		System.out.println("Server closed, closing connection ...");
        		System.exit(1);
        	} else if (s.startsWith("[b]")) {
        		System.out.println(s);
        		try {
					return this.in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
    	}
		return s;
	}
	
	public void startClient(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        System.out.println("Client Starting on "+hostName+" : "+portNumber);
        
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
        	this.in = in;
            this.actionLoop(in, stdIn, out);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
	}
}
