import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentEntry {
	private String username, tour;
	private double balance;
	private Date dueDate;
	
	public PaymentEntry(String u, String t, double b, Date d) {
		username=u;
		tour=t;
		balance=b;
		dueDate=d;
	}
	
	public boolean paymentDue(Date todaysDate) {
		return todaysDate.compareTo(dueDate)>0;
	}
	
	public String getUser() {
		return username;
	}
	
	public String getTOur() {
		return tour;
	}
	
	public double deductBalance(double d) {
		if(d>=balance) {
			balance=0;
			return d-balance;
		}
		balance-=d;
		return 0;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void changeTour(String t) {
		tour = t;
	}
	
	public void changeCost(double d) {
		balance=d;
	}
	
	public String toString() {
		return username+"\n"+tour+"\n"+balance+"\n"+new SimpleDateFormat("MM/dd/yyyy").format(dueDate)+"\n";
	}
	
	public String getDate() {
		return new SimpleDateFormat("MM/dd/yyyy").format(dueDate);
	}
	
	public Date getDateD() {
		return dueDate;
	}
}
