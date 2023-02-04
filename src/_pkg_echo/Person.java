package _pkg_echo;

public class Person {
	private int age;
	private String name;
	private String phoneNumber;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String toString() {
		String st = ""+name+","+age+","+phoneNumber;
		return st;
	}
	
	public static Person toPerson(String s) {
		Person p = new Person();
		String[] infos = s.split(",");
		p.name = infos[0];
		p.age = Integer.valueOf(infos[1]);
		p.phoneNumber = infos[2];
		return p;
	}
}
