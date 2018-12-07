
public class User {
	private String userName, password;
	private boolean isAdmin;
	
	public User(String u,String p,boolean i) {
		userName = u;
		password = p;
		isAdmin=i;
	}
	
	public void changePassword(String p) {
		password=p;
	}
	
	public String getName() {
		return userName;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public boolean validateUser(String u, String p) {
		return u.equals(userName) && p.equals(password);
	}
	
	public boolean usernameExists(String u) {
		return u.equals(userName);
	}
	
	public String[] save() {
		return new String[] {userName,password,Boolean.toString(isAdmin)};
	}
	
}
