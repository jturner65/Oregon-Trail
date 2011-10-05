package model;

import java.util.ArrayList;

import core.Logger;
import core.Logger.Level;

/**
 * Party class that contains an array of persons that are members.
 * @author George Johnston
 */
public class Party {
	
	private ArrayList<Person> members = new ArrayList<Person>();
	private int money;
	private Pace currentPace;
	private Rations currentRations;
	
	
	/**
	 * If party is created before members, this constructor is used.
	 */
	public Party() {
		this.members = null;
	}
	
	/**
	 * If people are present before party is created, this constructor is used
	 * @param party Array of people to be initialized into party
	 */
	public Party(Pace pace, Rations rations, ArrayList<Person> party) {
		for(Person person : party) {
			if (person != null) {
				members.add(person);
			}
		}
		this.money = 0;
		this.currentPace = pace;
		this.currentRations = rations;
		String partyCreationLog = members.size() + " members were created successfully: ";
		for (Person person: party){
			
			this.money += person.getProfession().getMoney();
			Logger.log(person.getName()+ " as a " + person.getProfession() + " brings $" + 
					person.getProfession().getMoney() + " to the party.", Logger.Level.INFO);

			partyCreationLog += person.getName() + " ";
		}
		Logger.log(partyCreationLog, Logger.Level.INFO);
		Logger.log("Party starting money is: $" + money, Logger.Level.INFO);
		Logger.log("Current pace is: " + currentPace + " and current rations is: " + currentRations, Level.INFO);
	}
	
	/**
	 * Returns an array of Persons present in the party
	 * @return
	 */
	public ArrayList<Person> getPartyMembers() {
		return this.members;
	}
	
	/**
	 * Returns the money
	 * @return the party's money
	 */
	public int getMoney(){
		return this.money;
	}
	
	/**
	 * Sets the party's money
	 * @param money
	 */
	public void setMoney(int money){
		this.money = money;
	}
	
	/**
	 * The possible values of the party's pace
	 * @author 
	 *
	 */
	
	public enum Pace{
		STEADY ("Steady"),
		STRENUOUS ("Strenuous"),
		GRUELING ("Grueling");
		
		private final String name;
		private Pace(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * 
	 * @return the party's pace setting
	 */
	public Pace getPace(){
		return currentPace;
	}
	
	/**
	 * 
	 * @param pace the party's new pace
	 */
	public void setPace(Pace pace){
		this.currentPace = pace;
	}
	
	/**
	 * The possible settings of the party's consumption rate
	 * @author 
	 *
	 */
	
	public enum Rations{
		FILLING ("Filling"),
		MEAGER ("Meager"),
		BAREBONES ("Barebones");
		
		private final String name;
		private Rations(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * 
	 * @return the party's current ration consumption rate
	 */
	public Rations getRations(){
		return currentRations;
	}
	
	/**
	 * 
	 * @param rations the desired new ration consumption rate
	 */
	public void setRations(Rations rations){
		this.currentRations = rations;
	}
}
