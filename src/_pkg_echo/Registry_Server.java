package _pkg_echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Registry_Server extends Server implements Registry_itf{
	List<Person> registry;
	enum state {
			WAITING, 
			ADD,
			GET,
			GETALL,
			SEARCH
	}
	state currentState;
	@Override
	public void add(Person p) {
		registry.add(p);
	}

	@Override
	public String getPhone(String name) {
		Person p = this.search(name);
		if (p != null) {
			return p.getPhoneNumber();
		}
		return null;
	}

	@Override
	public Iterable<Person> getAll() {
		Iterable<Person> iter = registry;
		return iter;
	}

	@Override
	public Person search(String name) {
		Iterable<Person> iterable = this.getAll();
		for (Person p : iterable) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}
	
	public Registry_Server() {
		registry = new ArrayList<Person>(); 
		currentState = state.WAITING;
	}
	
	public void actionLoop(BufferedReader in, PrintWriter out) throws NumberFormatException, IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	try {
	            switch(currentState) {
	            	case WAITING : 
	            		switch (Integer.valueOf(inputLine)) {
		            		case 0 : 
		            			currentState = state.ADD;
		            			break;
		            		case 1 : 
		            			currentState = state.GET;
		            			break;
		            		case 2 : 
		            			currentState = state.GETALL;
		            			break;
		            		case 3 : 
		            			currentState = state.SEARCH;
		            			break;
		            		default:
		            			break;
	            		}
	            		break;
	            	case GET:
	            		String phone = this.getPhone(inputLine);
	            		out.println(phone);
	            		this.currentState = state.WAITING;
	            		break;
	            	case SEARCH:
	            		Person p = this.search(inputLine);
	            		out.println(p.toString());
	            		this.currentState = state.WAITING;
	            		break;
				case ADD:
					Person p2 = Person.toPerson(inputLine);
					registry.add(p2);
					out.println("0"); // ACKNOWLEDGE
					this.currentState = state.WAITING;
					break;
				case GETALL:
					String s = "";
					for (Person person : registry) {
						s += person.toString()+"|";
					}
					out.println(s);
					this.currentState = state.WAITING;
					break;
				default:
					break;
	            }
        	} catch (Exception exp) {
        		System.out.println("Exception caught during arguments parsing : \n"+exp.getStackTrace()+"\nClosing server.");
        		out.println(this.EOT_s);
        		System.exit(1);
        	}
        }
	}
	
	public static void main(String[] args) {
		Registry_Server server = new Registry_Server();
		server.startServer(args);
	}
	
	

}
