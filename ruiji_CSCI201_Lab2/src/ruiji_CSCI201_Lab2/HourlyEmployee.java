package ruiji_CSCI201_Lab2;

public class HourlyEmployee extends Employee {
	private double hourly_rate;
	private double hours_worked;
	
	public HourlyEmployee(String fn, String ln, String bd, int id, String jt, String com, double hr, double hm) {
		super(fn, ln, bd, id, jt, com);
		this.hourly_rate = hr;
		this.hours_worked = hm;
	}
	
	public double getAnnualSalary() {
		return hourly_rate * hours_worked * 52;
	}
}
