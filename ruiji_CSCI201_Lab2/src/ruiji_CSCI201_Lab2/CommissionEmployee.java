package ruiji_CSCI201_Lab2;

public class CommissionEmployee extends SalariedEmployee{
	private double sales_total;
	private double commission_percentage;
	
	public CommissionEmployee(String fn, String ln, String bd, int id, String jt, String com, double as, double st, double cp) {
		super(fn, ln, bd, id, jt, com, as);
		this.sales_total = st;
		this.commission_percentage = cp;
	}
	
	public double getAnnualSalary() {
		return super.getAnnualSalary() + sales_total * commission_percentage;
	}
}
