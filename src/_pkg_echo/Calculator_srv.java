package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Calculator_srv extends Server implements Calculator_itf {
	
    private int currentA = 0;
    private int currentB = 0;
    private String operand = "";
    private char EOT = 0x04;
    private String EOT_s = String.valueOf(EOT);
    private int state; // 0:currentA, 1:operand, 2:currentB
    
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
