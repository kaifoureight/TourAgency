import java.util.ArrayList;

public class Manifest {
	private String tourName;
	private ArrayList<String> bookedUsers=new ArrayList<String>();
	
	public Manifest(String t) {
		tourName=t;
	}
	
	public void addBookedUser(String username) {
		bookedUsers.add(username);
	}
	
	public String toString() {
		String output = tourName+"\n";
		for(String user: bookedUsers) {output+=user+"\n";}
		return output;
	}
	
	public boolean isBooked(String user) {
		return bookedUsers.contains(user);
	}
	
	public void changeName(String n) {
		tourName=n;
	}
	
	public String getName() {
		return tourName;
	}
	
	public void removeUser(String user) {
		bookedUsers.remove(user);
	}
	
	public ArrayList<String> getBookedUsers(){
		return bookedUsers;
	}
	
}
