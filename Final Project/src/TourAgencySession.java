import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class TourAgencySession {
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Tour> tours = new ArrayList<Tour>();
	private ArrayList<Manifest> manifests = new ArrayList<Manifest>();
	private ArrayList<PaymentEntry> paymentEntries = new ArrayList<PaymentEntry>();
	private User currentUser = null;
	private Scanner scanner = new Scanner(System.in);
	private BufferedWriter writer;
	private BufferedReader reader;
	private String input; 
	private Date currentDate=null;
	//driver
	public static void main(String[] args) {
		TourAgencySession ta = new TourAgencySession();
	}
	//sets todays date for demonstration purposes
	public TourAgencySession(){
		System.out.println("What is today's date?(MM/dd/yyyy)\n: ");
		while(currentDate==null) {
			input = scanner.nextLine();
			    try {currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(input);} 
			    catch (ParseException e) {System.out.println("invalid date, please try again\n:");}
			}
		load();
		authenticate();
		scanner.close();
	}
	//lets registered users log in and unregistered users to create accounts
	public void authenticate() {
		String password, username;
		System.out.print("\nThank You For Choosing Margaret Thatcher's Tour Agency Software!\n\n(1)\tlogin to existing account\n(2)\tcreate new account\n(3)\texit program\n\n: ");
		while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")) {System.out.println("invalid selection. please try again");}
		if(input.equals("1")) {		
			while(currentUser==null) {
				System.out.println("\nUsername: ");
				username = scanner.nextLine();
				System.out.println("Password: ");
				password = scanner.nextLine();
				for(User u:users) {
					if(u.validateUser(username, password)){currentUser=u;}
				}
				if(currentUser==null) {System.out.println("Invalid credentials, please try again");}
			}
			presentMenu(true);
		}
		else if(input.equals("2")){
			while(currentUser==null) {
				boolean userExists=false;
				System.out.println("\nNew Username: ");
				username = scanner.nextLine();
				System.out.println("New Password: ");
				password = scanner.nextLine();
				for(User u:users) {userExists = userExists || u.usernameExists(username);}
				if(userExists) {System.out.println("Username taken, please try again");}
				else {
					if(username.contains(" ")||username.contains("\n")) {System.out.println("Username contains invalid characters, please try again");}
					else {
						currentUser = new User(username,password,false);
						System.out.println("Account succesfully created!");
					}
				}
			}
			users.add(currentUser);
			presentMenu(false);
		}
		else {saveExit();}
	}
	//logs the current user out and presents the main menu
	public void logout() {
		currentUser=null;
		authenticate();
	}
	//lets admins add tours
	public void addTours() {
		String n=null, d, dStart, dEnd, dName, dDesc;
		double cost=-1;
		Date d1=null,d2=null;
		System.out.println("\nName of new Tour: ");
		while(n==null) {
			n=scanner.nextLine();
			for(Tour t:tours) {
				if(t.getName().equals(n)) {
					n=null;
					System.out.println("A tour with this name already exists, please try again\n: ");
				}
			}
		}
		System.out.println("Description of new Tour: ");
		d=scanner.nextLine();
		System.out.println("Start Date: ");
		
		while(d1==null) {
			input = scanner.nextLine();
			    try {d1 = new SimpleDateFormat("MM/dd/yyyy").parse(input);} 
			    catch (ParseException e) {System.out.println("invalid date, please try again\n:");}
		}
		dStart = input;
		
		System.out.println("End Date: ");	
		while(d2==null) {
			input = scanner.nextLine();
			    try {d2 = new SimpleDateFormat("MM/dd/yyyy").parse(input);} 
			    catch (ParseException e) {System.out.println("invalid date, please try again\n:");}
		}
		dEnd = input;
		
		System.out.println("Total Cost: ");
		while(cost<=0) {
			try {cost=Double.parseDouble(scanner.nextLine());}
			catch(NumberFormatException e) {
				System.out.println("Please enter a valid cost\n: ");
				cost=-1;
			}
		}
		Tour newTour = new Tour(n,d,dStart,dEnd);
		newTour.getItinerary().setTotalCost(cost);
		System.out.println("Let's add the destinations in this tour\n");
		inputLoop: while(true) {
			System.out.print("Destination Name: ");
			dName = scanner.nextLine();
			System.out.println("Destination Description: ");
			dDesc = scanner.nextLine();
			newTour.getItinerary().add(new Destination(dName,dDesc));
			System.out.print("\n\nDestination '"+dName+"' succesfully added to '"+d+"'. Would you like to add more destinations? (y/n): ");
			input = scanner.nextLine();
			while(!input.equals("y")&&!input.equals("n")) {
				System.out.println("Invalid input, please try again\n: ");
				input = scanner.nextLine();
			}
			if(input.equals("n")) {break inputLoop;}
		}
		tours.add(newTour);
		presentMenu(false);
	}
	//lets admins manage the tours
	public void updateTours() {
		Tour modifying = null;
		System.out.println("\n\nWhich tour would you like to update? ");
		for(int i=0;i<tours.size();i++) {System.out.println("("+(i+1)+")\t"+tours.get(i).getName());}
		System.out.println(": ");	
		while(modifying == null) {
			while(!scanner.hasNextInt()) {
				System.out.println("Invalid input, please try again\n: ");
				scanner.nextLine();
			}
			input = scanner.nextLine();
			if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=tours.size()){modifying = tours.get(Integer.parseInt(input)-1);}
			else {System.out.println("Tour not found, please try again: ");}
		}
		modifyLoop: while(true) {		
			System.out.print("\n\nWhat would you like to modify:\n(1)\tName\n(2)\tDescription\n(3)\tItineraries\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {
				renameLoop:while(true) {
					System.out.println("New name for tour '"+modifying.getName()+"': ");
					input=scanner.nextLine();
					for(Tour t:tours) {
						if(t.getName().equals(input)) {
							input=null;
							System.out.println("A tour with this name already exists, please try again\n: ");
						}
					}
					if(input!=null) {
						for(Manifest m: manifests) {
							if(m.getName().equals(modifying.getName())) {m.changeName(input);}
						}
						for(PaymentEntry p:paymentEntries) {
							if(p.getTOur().equals(modifying.getName())) {p.changeTour(input);}
						}
						modifying.setName(input);
						break renameLoop;
					}
				}
			}
			else if(input.equals("2")) {
				System.out.println("New description for tour '"+modifying.getName()+"': ");
				input=scanner.nextLine();
				modifying.setDesc(input);
			}
			else if(input.equals("3")) {modifyItinerary(modifying);}
			System.out.println("\nAre you done modifying Tour '"+modifying.getName()+"'? (y/n)\n: ");
			input = scanner.nextLine();
			while(!input.equals("y")&&!input.equals("n")) {
				System.out.println("Invalid input, please try again\n: ");
				input = scanner.nextLine();
			}
			if(input.equals("y")) {break modifyLoop;}
		}
		presentMenu(false);
	}
	//lets admins manage the itinerary of a tour
	public void modifyItinerary(Tour t) {
		Destination modifying = null;
		System.out.println("Which destination in the itinerary would you like to modify?");
		for(int i=0;i<t.getItinerary().getDestinations().size();i++) {System.out.println("("+(i+1)+")\t"+t.getItinerary().getDestinations().get(i).getName());}
		System.out.println(": ");
		while(modifying == null) {
			while(!scanner.hasNextInt()) { 
				System.out.println("Invalid input, please try again\n: ");
				scanner.nextLine();
			}
			input = scanner.nextLine();
			if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=t.getItinerary().getDestinations().size()){modifying = t.getItinerary().getDestinations().get(Integer.parseInt(input)-1);}
			else {System.out.println("Destination not found, please try again: ");}
		}
		inputLoop: while(true) {
			System.out.println("What would you like to modify?\n(1)\tName\n(2)\tDescription\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {
				System.out.println("New name for destination '"+modifying.getName()+"': ");
				input=scanner.nextLine();
				modifying.setName(input);
			}
			else if(input.equals("2")) {
				System.out.println("New description for destination '"+modifying.getName()+"': ");
				input=scanner.nextLine();
				modifying.setDescription(input);
			}
			System.out.println("\nAre you done modifying Destination '"+modifying.getName()+"'? (y/n)\n: ");
			input = scanner.nextLine();
			while(!input.equals("y")&&!input.equals("n")) {
				System.out.println("Invalid input, please try again\n: ");
				input = scanner.nextLine();
			}
			if(input.equals("y")) {break inputLoop;}
		}
	}
	//presents the menu to all users
	public void presentMenu(boolean reminder) {
		if(currentUser.isAdmin()) {
			System.out.print("\n\nWelcome To The Admin Menu!\n(1)\tLogout\n(2)\tAdd Tours\n(3)\tUpdate Tours\n(4)\tManage Users\n(5)\tPrepare Reports\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {logout();}
			else if(input.equals("2")) {addTours();}
			else if(input.equals("3")) {updateTours();}
			else if(input.equals("4")) {manageUsers();}
			else if(input.equals("5")) {prepareReports();}	
		}
		else {
			if(reminder) {
				ArrayList<PaymentEntry> payments = new ArrayList<PaymentEntry>();
				for(PaymentEntry p: paymentEntries) {
					if(p.getUser().equals(currentUser.getName())) {payments.add(p);}
				}
				if(payments.size()>0) {
					for(PaymentEntry p:payments) {
						if(p.getDateD().before(currentDate)) {
							for(Manifest m:manifests) {if(m.getName().equals(p.getTOur())&&m.isBooked(currentUser.getName())) {m.removeUser(currentUser.getName());}}
							System.out.println("\nYOUR BOOKING FOR '"+p.getTOur()+"' HAS PASSED ITS PAYMENT PERIOD. YOU HAVE BEEN AUTOMATICALLY UBOOKED\n");
							paymentEntries.remove(p);
						}
					}
				}
				payments = new ArrayList<PaymentEntry>();
				for(PaymentEntry p: paymentEntries) {
					if(p.getUser().equals(currentUser.getName())) {payments.add(p);}
				}
				if(payments.size()>0) {
					System.out.println("\nReminder: You currently have "+payments.size()+" outstanding payments:");
					for(PaymentEntry p:payments) {System.out.println("Payment of $"+p.getBalance()+" for Tour '"+p.getTOur()+"' due by "+p.getDate());}
				}	
			}
			System.out.print("\n\nWelcome To The Main Menu!\n(1)\tLogout\n(2)\tManage Balance\n(3)\tManage Booked Tours\n(4)\tView all tours\n(5)\tBook New Tour\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {logout();}
			else if(input.equals("2")) {manageBalance();}
			else if(input.equals("3")) {manageTours();}
			else if(input.equals("4")) {viewAllTours();}
			else if(input.equals("5")) {bookTour(true);}
		}
	}
	//lets admins manage all registered users
	public void manageUsers() {
		User modifying = null;
		System.out.print("\nAll registered users: \n");
		for(int i=0;i<users.size();i++) {System.out.println("("+(i+1)+")\t"+users.get(i).getName());}
		System.out.print("\nWhich user account would you like to manage?\n: ");
		while(modifying== null) {
			while(!scanner.hasNextInt()) {
				System.out.println("Invalid input, please try again\n: ");
				scanner.nextLine();
			}
			input = scanner.nextLine();
			if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=users.size()){modifying = users.get(Integer.parseInt(input)-1);}
			else {System.out.println("User not found, please try again: ");}
		}
		System.out.println("\nWhat would you like to do?\n(1)\tView all bookings\n(2)\tView all payments due\n(3)\tChange password\n(4)\tExit\n: ");
		while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")) {System.out.println("invalid selection. please try again");}
		if(input.equals("1")) {
			System.out.println("\nReport: All bookings by "+modifying.getName()+"\n");
			for(Manifest m: manifests) {
				if(m.isBooked(modifying.getName())) {System.out.println("----------------------------\nTour: '"+m.getName());}
			}
		}
		else if(input.equals("2")) {
			System.out.println("\nReport: All payment logs by "+modifying.getName()+"\n");
			for(PaymentEntry p: paymentEntries) {
				if(p.getUser().equals((modifying.getName()))) {System.out.println("----------------------------\nUser: "+p.getUser()+"\nTour: "+p.getTOur()+"\nBalance: "+p.getBalance()+"\nDue Date: "+p.getDate());}
			}
		}
		else if(input.equals("3")) {
			System.out.println("\nNew Password: ");
			modifying.changePassword(scanner.next());
		}
		presentMenu(false);
	}
	//prepares a report for admins
	public void prepareReports() {
		reportLoop:while(true) {
			System.out.println("\nWhat report would you like to prepare?\n(1)\tCurrent Tours\n(2)\tRegistered Users\n(3)\tPayment Logs\n(4)\tTour Manifests\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {
				System.out.println("\nReport: All current tours\n");
				for(Tour t: tours) {
					System.out.println("----------------------------\n"+t.getName()+"\nDescription: "+t.getDesc()+"\nTotal Cost: "+t.getCost()+"\nStart/End Dates: "+t.getDate());
					for(int i=0;i<t.getItinerary().getDestinations().size();i++) {
						System.out.println("\nDestination "+(i+1)+": "+t.getItinerary().getDestinations().get(i).getName()+"\nDescription: "+t.getItinerary().getDestinations().get(i).getDescription());
					}
				}
			}
			if(input.equals("2")) {
				System.out.println("\nReport: All registered Users\n");
				for(User u:users) {System.out.println("----------------------------\nUsername: "+u.getName());}
			}
			if(input.equals("3")) {
				System.out.println("\nReport: All payment logs\n");
				for(PaymentEntry p: paymentEntries) {
					System.out.println("----------------------------\nUser: "+p.getUser()+"\nTour: "+p.getTOur()+"\nBalance: "+p.getBalance()+"\nDue Date: "+p.getDate());
				}
			}
			if(input.equals("4")) {
				System.out.println("\nReport: All Manifests\n");
				for(Manifest m: manifests) {
					System.out.println("----------------------------\nTour: '"+m.getName()+"' with the following booked users:");
					for(String user: m.getBookedUsers()) {System.out.println(user);}
				}
			}
			System.out.print("----------------------------\n\nWould you prepare another report? (y/n)\n: ");
			input = scanner.nextLine();
			while(!input.equals("y")&&!input.equals("n")) {
				System.out.println("Invalid input, please try again\n: ");
				input = scanner.nextLine();
			}
			if(input.equals("n")) {break reportLoop;}
		}
		presentMenu(false);
	}
	//lets users manage their balance
	public void manageBalance() {
		String card = "";
		double paymentAmount = -1;
		PaymentEntry modifying = null;
		ArrayList<PaymentEntry> zeroes = new ArrayList<PaymentEntry>();
		for(PaymentEntry p:paymentEntries) {
			if(p.getBalance()<=0.01) {zeroes.add(p);}
		}
		for(PaymentEntry p:zeroes) {
			paymentEntries.remove(p);
		}
		ArrayList<PaymentEntry> payments = new ArrayList<PaymentEntry>();
		for(PaymentEntry p: paymentEntries) {
			if(p.getUser().equals(currentUser.getName())){payments.add(p);}
		}
		if(payments.size()==0) {
			System.out.println("\n\nYou currently have zero payments due.\n");
			presentMenu(false);
		}
		else {
			System.out.println("You currently have "+payments.size()+" payments due. Which would you like to manage?\n:");
			for(int i=0;i<payments.size();i++) {System.out.println("("+(i+1)+")\tPayment of $"+payments.get(i).getBalance()+" for '"+payments.get(i).getTOur()+"' due "+payments.get(i).getDate());}
		
			while(modifying == null) {
				while(!scanner.hasNextInt()) {
					System.out.println("Invalid input, please try again\n: ");
					scanner.nextLine();
				}
				input = scanner.nextLine();
				if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=tours.size()){modifying = payments.get(Integer.parseInt(input)-1);}
				else {System.out.println("Payment Entry not found, please try again: ");}
			}
			System.out.print("\n\nWhat would you like to do with the payment entry for "+modifying.getTOur()+"?\n(1)\tPay with Credit Card\n(2)\tPay with Debit Card\n(3)\tPay Later\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!input.equals("2")&&!input.equals("3")) {System.out.println("invalid selection. please try again");}
			if(input.equals("3")) {
				presentMenu(false);
			}
			else {
				if(input.equals("2")) {System.out.println("\nWhat is your 16 digit debit card number?\n: ");}
				else if(input.equals("1")) {System.out.println("\nWhat is your 16 digit credit card number?\n: ");}
				while(String.valueOf(card).length()!=16 || !card.matches("[0-9]+")) {
					card=scanner.nextLine();
					if(String.valueOf(card).length()!=16 || !card.matches("[0-9]+")) {System.out.println("Invalid card number. Please try again\n: ");}
				}
				System.out.println("\nHow much of your outstanding balance of $"+modifying.getBalance()+" would you like to pay?\n: ");
				while(paymentAmount<0 || paymentAmount>modifying.getBalance()) {
					try {paymentAmount=Double.parseDouble(scanner.nextLine());}
					catch(NumberFormatException e) {
						System.out.println("Please enter a valid payment amount\n: ");
						paymentAmount=-1;
					}
					if(paymentAmount>modifying.getBalance()) {System.out.println("You are paying more than the balance due. Please try again\n: ");}
				}
				modifying.deductBalance(paymentAmount);
				if(modifying.getBalance()<0.01) {
					System.out.println("\nYou have fully paid for '"+modifying.getTOur()+"'. Enjoy your tour!\n");
					paymentEntries.remove(modifying);
				}
				else {System.out.println("\nPayment succesful, your new balance for '"+modifying.getTOur()+"' is $"+modifying.getBalance()+", due "+modifying.getDate());}
				presentMenu(false);
			}
		}
		
	}
	//lets users manage their booked tours
	public void manageTours() {
		Tour mTour=null;
		PaymentEntry removeP=null;
		ArrayList<String> bookedTours = new ArrayList<String>();
		for(Manifest m:manifests) {
			if(m.isBooked(currentUser.getName())) {bookedTours.add(m.getName());}
		}
		if(bookedTours.size()==0) {System.out.println("\nSorry, you currently have 0 booked bookedTours");}
		else {
			System.out.println("\nWhich tour would you like to manage?\n: ");
			for(int i=0;i<bookedTours.size();i++) {
				System.out.println("("+(i+1)+")\t"+bookedTours.get(i));
			}	
			while(mTour == null) {
				while(!scanner.hasNextInt()) {
					System.out.println("Invalid input, please try again\n: ");
					scanner.nextLine();
				}
				input = scanner.nextLine();
				if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=bookedTours.size()){
					for(Tour t: tours) {
						if(t.getName().equals(bookedTours.get(Integer.parseInt(input)-1))) {mTour=t;}
					}
				}
				else {System.out.println("Tour not found, please try again: ");}
				
			}
			System.out.print("\n\nWhat would you like to do with tour '"+mTour.getName()+"'?\n(1)\tUnbook\n(2)\tView Details\n: ");
			while(!(input = scanner.nextLine()).equals("1")&&!(input.equals("2"))) {System.out.println("invalid selection. please try again");}
			if(input.equals("1")) {
				for(PaymentEntry p:paymentEntries) {
					System.out.println(p.toString());
					if(p.getTOur().equals(mTour.getName())&&p.getUser().equals(currentUser.getName())) {removeP=p;}
				}
				System.out.print("Tour unbooked. You have been refunded $"+(mTour.getCost()-removeP.getBalance()));
				for(Manifest m:manifests) {if(m.isBooked(currentUser.getName())&&m.getName().equals(mTour.getName())) {m.removeUser(currentUser.getName());}}
				paymentEntries.remove(removeP);
			}
			else if(input.equals("2")) {
				System.out.println("View Tour Details:\n");
				System.out.println("----------------------------\n"+mTour.getName()+"\nDescription: "+mTour.getDesc()+"\nTotal Cost: "+mTour.getCost()+"\nStart/End Dates: "+mTour.getDate());
				for(int i=0;i<mTour.getItinerary().getDestinations().size();i++) {
					System.out.println("\nDestination "+(i+1)+": "+mTour.getItinerary().getDestinations().get(i).getName()+"\nDescription: "+mTour.getItinerary().getDestinations().get(i).getDescription());
				}
			}
		}
		presentMenu(false);
	}
	//lets users view all available tours
	public void viewAllTours() {
		System.out.println("All Available Tours:\n");
		for(Tour t: tours) {
			System.out.println("----------------------------\n"+t.getName()+"\nDescription: "+t.getDesc()+"\nTotal Cost: "+t.getCost()+"\nStart/End Dates: "+t.getDate());
			for(int i=0;i<t.getItinerary().getDestinations().size();i++) {
				System.out.println("\nDestination "+(i+1)+": "+t.getItinerary().getDestinations().get(i).getName()+"\nDescription: "+t.getItinerary().getDestinations().get(i).getDescription());
			}
		}
		System.out.print("----------------------------\n\nWould you like to book a tour? (y/n)\n: ");
		input = scanner.nextLine();
		while(!input.equals("y")&&!input.equals("n")) {
			System.out.println("Invalid input, please try again\n: ");
			input = scanner.nextLine();
		}
		if(input.equals("y")) {bookTour(false);}
		if(currentUser!=null) {presentMenu(false);}
	}
	//lets users book tours, presentMenu dictates if the main menu should be displayed afterwards
	public void bookTour(boolean presentMenu) {
		Tour bookMe = null, mS = null;
		System.out.println("All available tours:");
		for(int i=0;i<tours.size();i++) {
			System.out.println("("+(i+1)+")\t"+tours.get(i).getName()+"\t"+tours.get(i).getDate());
		}
		System.out.println("Which tour would you like to book?\n: ");		
		while(bookMe == null) {
			while(!scanner.hasNextInt()) {
				System.out.println("Invalid input, please try again\n: ");
				scanner.nextLine();
			}
			input = scanner.nextLine();
			if(Integer.parseInt(input)>0 && Integer.parseInt(input)<=tours.size()){bookMe = tours.get(Integer.parseInt(input)-1);}
			else {System.out.println("Tour not found, please try again: ");}
		}
		System.out.println("\n----------------------------\n"+bookMe.getName()+"\nDescription: "+bookMe.getDesc()+"\nTotal Cost: "+bookMe.getCost()+"\nStart/End Dates: "+bookMe.getDate());
		for(int i=0;i<bookMe.getItinerary().getDestinations().size();i++) {
			System.out.println("\nDestination "+(i+1)+": "+bookMe.getItinerary().getDestinations().get(i).getName()+"\nDescription: "+bookMe.getItinerary().getDestinations().get(i).getDescription());
		}
		System.out.print("----------------------------\n\nWould you like to book this tour for $"+bookMe.getCost()+"? (y/n)\n: ");
		input = scanner.nextLine();
		while(!input.equals("y")&&!input.equals("n")) {
			System.out.println("Invalid input, please try again\n: ");
			input = scanner.nextLine();
		}
		if(input.equals("y")) {
			boolean isbooked = false, timeConflict=false;
			for(Manifest m: manifests) {
				for(Tour t: tours) {if(t.getName().equals(m.getName())){mS=t;}}
				isbooked = isbooked || m.getName().equals(bookMe.getName())&&m.isBooked(currentUser.getName());
				timeConflict = isbooked && (timeConflict || (mS.getStartDate().before(bookMe.getStartDate()) && mS.getEndDate().after(bookMe.getStartDate())) || (mS.getStartDate().before(bookMe.getEndDate()) && 
						mS.getEndDate().after(bookMe.getEndDate())) || (mS.getStartDate().before(bookMe.getStartDate()) && mS.getEndDate().after(bookMe.getEndDate())));
			}
			if(isbooked){System.out.println("\nSorry, but you have already booked this tour!\n");			}
			else if(timeConflict) {System.out.println("\nSorry, but this tour's schedule conflicts with another tour you have booked!\n");}
			else {
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 20);
				for(Manifest m:manifests) {
					if(m.getName().equals(bookMe.getName())) {m.addBookedUser(currentUser.getName());}
				}	
				paymentEntries.add(new PaymentEntry(currentUser.getName(),bookMe.getName(),bookMe.getCost(),c.getTime()));
				System.out.println("\nTour succesfully booked. Payment of $"+bookMe.getCost()+" due at "+new SimpleDateFormat("MM/dd/yyyy").format(c.getTime()));
			}
			presentMenu(false);
		}	
		else {bookTour(false);}
		if(presentMenu && currentUser!=null) {presentMenu(false);}
	}
	//loads data from files for persistence
	public void load() {
		try {
			String line, toursFile="", manifestFile="", paymentFile="";
			reader = new BufferedReader(new FileReader("users"));
			while ((line = reader.readLine()) != null) {if(!line.equals(null)&&!line.equals("")&&!line.equals("\n")) {users.add(new User(line,reader.readLine(),Boolean.parseBoolean(reader.readLine())));} }
			reader = new BufferedReader(new FileReader("tours"));
			while ((line = reader.readLine()) != null) {toursFile+=line+"\n";}
			String[] tour_entries = toursFile.split("\n\n");
			for(int i=0;i<tour_entries.length;i++) {
				String[] tourL = tour_entries[i].split("\n");
				Tour tour = new Tour(tourL[0],tourL[1],tourL[2],tourL[3]);
				tour.getItinerary().setTotalCost(Double.parseDouble(tourL[4]));
				for(int j=0;j<(tourL.length-5)/2;j++) {tour.getItinerary().add(new Destination(tourL[5+2*j],tourL[6+2*j]));}
				tours.add(tour);
			}
			reader = new BufferedReader(new FileReader("manifests"));
			while ((line = reader.readLine()) != null) {manifestFile+=line+"\n";}
			String[] manifest_entries = manifestFile.split("\n\n");
			for(int i=0;i<manifest_entries.length;i++) {
				String[] manifestL = manifest_entries[i].split("\n");
				if(manifestL.length>=1) {
					Manifest entry = new Manifest(manifestL[0]);
					for(int j=1;j<manifestL.length;j++) {entry.addBookedUser(manifestL[j]);}	
					manifests.add(entry);	
				}
			}
			reader = new BufferedReader(new FileReader("paymentEntries"));
			while ((line = reader.readLine()) != null) {paymentFile+=line+"\n";}
			String[] payment_entries = paymentFile.split("\n\n");
			for(int i=0;i<payment_entries.length;i++) {
				String[] paymentL = payment_entries[i].split("\n");
				if(paymentL.length>=3) {
					try {paymentEntries.add(new PaymentEntry(paymentL[0],paymentL[1],Double.parseDouble(paymentL[2]),new SimpleDateFormat("MM/dd/yyyy").parse(paymentL[3])));}
					catch (NumberFormatException e) {} catch (ParseException e) {}
				}
			}
			System.out.println(paymentEntries.size());
		} catch (IOException e) {e.printStackTrace();}
	}
	//saves data to files for persistence
	public void saveExit() {
		try {
		    writer = new BufferedWriter(new FileWriter("users",false));
		    for(User u:users) {for(String str: u.save()) {writer.write(str+"\n");}}
		    writer.close();
		    writer = new BufferedWriter(new FileWriter("tours",false));
		    for(Tour t: tours) {writer.write(t.save()+"\n");}
		    writer.close();
		    writer = new BufferedWriter(new FileWriter("manifests",false));
		    for(Manifest m: manifests) {writer.write(m.toString()+"\n");}		    
		    writer.close();
		    writer = new BufferedWriter(new FileWriter("paymentEntries",false));
		    for(PaymentEntry p: paymentEntries) {writer.write(p.toString()+"\n");}		    
		    writer.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}
