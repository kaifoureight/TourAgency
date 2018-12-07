import java.util.ArrayList;

public class Itinerary {
	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	double totalCost;
	
	public Itinerary() {
		
	}
	
	public void add(Destination d) {
		destinations.add(d);
	}
	
	public void remove(Destination d) {
		destinations.remove(d);
	}
	
	public ArrayList<Destination> getDestinations(){
		return destinations;
	}
	
	public void setTotalCost(double d) {
		totalCost=d;
	}
	
	public double getTotalCost() {
		return totalCost;
	}
	
	public String print() {
		String output = totalCost+"\n";
		for(Destination d: destinations) {
			output+=d.toString()+"\n";
		}
		return output;
	}

}
