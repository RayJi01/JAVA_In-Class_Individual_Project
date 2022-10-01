package ruiji_CSCI201_Lab2;

public class Person {
	private String firstname;
	private String lastname;
	private String birthdate;
	
	public Person(String fn, String ln, String bd) {
		this.firstname = fn;
		this.lastname = ln;
		this.birthdate = bd;
	}
	
	public String getfn() {
		return firstname;
	}
	public String getln() {
		return lastname;
	}
	public String getbd() {
		return birthdate;
	}
}

