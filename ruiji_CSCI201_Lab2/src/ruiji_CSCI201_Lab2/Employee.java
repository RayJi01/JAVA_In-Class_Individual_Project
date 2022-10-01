package ruiji_CSCI201_Lab2;

public abstract class Employee extends Person {
	
	private int employee_ID;
	private String job_title;
	private String company;
	
	public Employee(String fn, String ln, String bd, int id, String jt, String com) {
		super(fn, ln, bd);
		this.employee_ID = id;
		this.job_title = jt;
		this.company = com;
	}
	
	public String getFirstName() {
		return this.getfn();
	}
	
	public String getLastName() {
		return this.getln();
	}
	
	public String getBirthdate() {
		return this.getbd();
	}
	
	public String getJobTitle() {
		return this.job_title;
	}
	
	public String getCompany() {
		return this.company;
	}
	
	public int getEmployeeID() {
		return this.employee_ID;
	}
	
	public abstract double getAnnualSalary();
}
