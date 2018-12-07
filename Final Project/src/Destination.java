
public class Destination {
	private String name, description;

	public Destination(String n, String d) {
		name=n;
		description=d;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return name+"\n"+description;
	}
}
