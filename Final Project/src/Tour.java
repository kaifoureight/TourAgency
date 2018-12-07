import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tour {
	private String name, description;
	private Date startDate, endDate;
	private Itinerary itinerary = new Itinerary();
	
	public Tour(String n, String d, String sD, String eD) {
		name = n;
		description = d;
		try {
			startDate = new SimpleDateFormat("MM/dd/yyyy").parse(sD);
			endDate = new SimpleDateFormat("MM/dd/yyyy").parse(eD);
		} 
	    catch (ParseException e) {System.out.println("invalid date, please try again\n:");}
	}
	
	public double getCost() {
		return itinerary.getTotalCost();
	}
	
	public void setName(String n) {
		name=n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDesc(String s) {
		description = s;
	}
	
	public String getDesc() {
		return description;
	}
	
	public String getDate() {
		return new SimpleDateFormat("MM/dd/yyyy").format(startDate)+" - "+new SimpleDateFormat("MM/dd/yyyy").format(endDate);
	}
	
	public Itinerary getItinerary() {
		return itinerary;
	}
	
	public String printDetails() {
		String itin = "";
		for(Destination d: itinerary.getDestinations()) {
			itin += "\n\n-----"+d.getName()+"\n\n"+d.getDescription()+"\n-----";
		}
		return name+"\n\n"+new SimpleDateFormat("MM/dd/yyyy").format(startDate)+" - "+new SimpleDateFormat("MM/dd/yyyy").format(endDate)+"\n\n"+description+itin;
	}
	
	public String save() {
		String output = name+"\n"+description+"\n"+new SimpleDateFormat("MM/dd/yyyy").format(startDate)+"\n"+new SimpleDateFormat("MM/dd/yyyy").format(endDate)+"\n"+itinerary.print();
		return output;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
}
