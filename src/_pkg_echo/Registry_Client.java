package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Registry_Client extends Client{

	
	public void actionLoop(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
		String userInput;
		System.out.println("What do you wish for ?");
    	System.out.println("# Get Number : 1\n# Get Person : 2\n# Get All Persons : 3\n# Add Person to Registry : 4\n");
        do {
        	System.out.print("> ? : ");
        	userInput = stdIn.readLine();
        	out.println(userInput);
        	String acknowledge = this.process(in.readLine());
        	switch (acknowledge) {
	        	case "1":
	        		System.out.print("Name ? : ");
	        		out.println(stdIn.readLine());
	        		System.out.println(this.process(in.readLine()));
	        		break;
	        	case "2":
	        		System.out.print("Name ? : ");
	        		out.println(stdIn.readLine());
	        		Person p = Person.toPerson(this.process(in.readLine()));
	        		if (p == null) {
	        			System.out.println("This person was not found in the registry.");
	        		} else {
		        		System.out.println(p.getName()+", "+p.getAge()+" : "+p.getPhoneNumber());	
	        		}
	        		break;
	        	case "3":
	        		out.println("3");
	        		String registry = this.process(in.readLine());
	        		try {
	        			String[] reg_s = registry.split("\\|");
		        		for (String perso : reg_s) {
		        			Person p2 = Person.toPerson(perso);
		        			System.out.println(p2.getName()+", "+p2.getAge()+" : "+p2.getPhoneNumber());
		        		}
	        		} catch (Exception exp) {
	        			System.out.println("Registry is currently empty.");
	        		}
	        		break;
	        	case "4":
	        		Person p2 = new Person();
	        		System.out.print("Name ? : ");
	        		p2.setName(stdIn.readLine());
	        		System.out.print("Age ? : ");
	        		p2.setAge(Integer.valueOf(stdIn.readLine()));
	        		System.out.print("Phone Number ? : ");
	        		p2.setPhoneNumber(stdIn.readLine());
	        		out.println(p2.toString());
	        		String ack = this.process(in.readLine());
	        		if (Integer.valueOf(ack) != 0) {
	        			throw new IllegalArgumentException("Adding person ended in wrong server acknowledgement");
	        		}
	        		break;
	        	default:
	        		throw new IllegalArgumentException("Server sent unknown value");
        	}
        } while (userInput != null);
	}
	
	public static void main(String[] args) {
		Registry_Client client = new Registry_Client();
		client.startClient(args);
	}

}
