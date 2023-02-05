package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The Class Calculator_srv implements Calculator_itf 
 * and uses the generic function from the Server class 
 * to start the server. It listens to the client input
 * and sends back a result
 */
public class Calculator_srv extends Server implements Calculator_itf {
	
    private int currentA = 0;
    private int currentB = 0;
    private String operand = "";
    private int state; // 0:currentA, 1:operand, 2:currentB
    
    /**
     * Method plus does an addition of two integers
     * 
     * @param a - first integer
     * @param b - second integer
     * 
     * @return the sum of a and b
     */
	@Override
	public int plus(int a, int b) {
		return a+b;
	}

    /**
     * Method minus does a substraction of two integers
     * 
     * @param a - first integer
     * @param b - second integer
     * 
     * @return the substraction of a and b
     */
	@Override
	public int minus(int a, int b) {
		return a-b;
	}

    /**
     * Method divide does a division of two integers
     * 
     * @param a - first integer
     * @param b - second integer
     * 
     * @return the division of a by b
     */
	@Override
	public int divide(int a, int b) {
		return a/b;
	}

    /**
     * Method multiply does a multiplication of two integers
     * 
     * @param a - first integer
     * @param b - second integer
     * 
     * @return the multiplication of a and b
     */
	@Override
	public int multiply(int a, int b) {
		return a*b;
	}
	
	/**
     * Method run_calculation, runs calculations
     * 
     * @param currentA - an integer
     * @param operand - a string of an operand
     * @param currentB - an integer
     * 
     * @return the result of the calculation based on the operand
     * 
     * @throws IllegalArgumentException if the operand is unknown
     */
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
	
	/**
	 * The action loop method that handles the communication
	 * between the client and server.
	 * 
	 * @param in - BufferedReader to read input from the server
	 * @param stdIn - BufferedReader to read input from the user
	 * @param out - PrintWriter to send output to the server
	 * @throws IOException - If an input/output error occurs
	 */
	public void actionLoop(BufferedReader in, PrintWriter out) throws NumberFormatException, IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	try {
	            switch(state) {
	            	case 0 : 
	            		currentA = Integer.valueOf(inputLine);
	            		break;
	            	case 1:
	            		operand = inputLine;
	            		break;
	            	case 2:
	            		currentB = Integer.valueOf(inputLine);
	            		try {
	            			int result = run_calculation(currentA, operand, currentB);
	            			out.println(result);
	            		} catch (Exception ex) {
	            			System.out.println("Exception caught during calculation : \n"+ex+"\nClosing server ...");
	            			out.println(EOT_s);
	            			System.exit(1);
	            		}
	            	break;
	            }
	            state = (state+1)%3;
        	} catch (Exception exp) {
        		System.out.println("Exception caught during arguments parsing : \n"+exp.getStackTrace()+"\nClosing server.");
        		out.println(EOT_s);
        		System.exit(1);
        	}
        }
	}

	public static void main(String[] args) {
		Calculator_srv server = new Calculator_srv();
		server.startServer(args);
	}

}
