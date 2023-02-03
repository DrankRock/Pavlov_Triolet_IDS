package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Calculator_srv implements Calculator_itf {
	
    private int currentA = 0;
    private int currentB = 0;
    private String operand = "";
    private char EOT = 0x04;
    private String EOT_s = String.valueOf(EOT);
    
	@Override
	public int plus(int a, int b) {
		return a+b;
	}

	@Override
	public int minus(int a, int b) {
		return a-b;
	}

	@Override
	public int divide(int a, int b) {
		return a/b;
	}

	@Override
	public int multiply(int a, int b) {
		return a*b;
	}
	
	public int run_calculation(int currentA, String operand, int currentB) {
		switch(operand) {
		case "+":
			return plus(currentA, currentB);
		case "-":
			return minus(currentA, currentB);
		case "x":
		case "*":
			return multiply(currentA, currentB);
		case "/":
			return divide(currentA, currentB);
		}
		throw new IllegalArgumentException("Unknown Operand. Please use '+', '-', 'x', '/'");
	}
	
	public void waitForClient(BufferedReader in, PrintWriter out) throws NumberFormatException, IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	System.out.println("Received : '"+inputLine+"'");
        	try {
        		String[] toS = inputLine.split("|");
        		int iterator = Integer.valueOf(toS[0]);
        		String inputLine_2 = "";
        		for (int i = 2; i<toS.length;i++) {
        			inputLine_2+=toS[i];
        		}
	            switch(iterator) {
	            	case 0 : 
	            		currentA = Integer.valueOf(inputLine_2);
	            		break;
	            	case 1:
	            		operand = inputLine_2;
	            		break;
	            	case 2:
	            		currentB = Integer.valueOf(inputLine_2);
	            		try {
	            			int result = run_calculation(currentA, operand, currentB);
	            			System.out.println("Result : "+result);
	            			out.println(result);
	            		} catch (Exception ex) {
	            			System.out.println("Exception caught during calculation : \n"+ex+"\nClosing server ...");
	            			out.println(EOT_s);
	            			System.exit(1);
	            		}
	            	break;
	            }
        	} catch (Exception exp) {
        		System.out.println("Exception caught during arguments parsing : \n"+exp.getStackTrace()+"\nClosing server.");
        		out.println(EOT_s);
        		System.exit(1);
        	}
        }
	}
	
	public void startServer(String[] args) {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Server starting on port : "+portNumber);
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        	this.waitForClient(in, out);
            System.out.println("I'm ending...");
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
	}

	public static void main(String[] args) {
		Calculator_srv server = new Calculator_srv();
		server.startServer(args);
	}

}
