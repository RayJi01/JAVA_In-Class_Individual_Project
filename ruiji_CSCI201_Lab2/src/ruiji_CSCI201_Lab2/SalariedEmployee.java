package ruiji_CSCI201_Lab2;

public class SalariedEmployee extends Employee {
	private double annual_salary;
	
	public SalariedEmployee(String fn, String ln, String bd, int id, String jt, String com, double as) {
		super(fn, ln, bd, id, jt, com);
		this.annual_salary = as;
	}
	
	public double getAnnualSalary() {
		return this.annual_salary;
	}
}
