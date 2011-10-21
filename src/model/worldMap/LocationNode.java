package model.worldMap;

import java.util.ArrayList;
import java.util.List;

/**
 * class responsible for holding the information about a location the party can travel to.
 * @author NULL&&void
 *
 */

public class LocationNode {
	
	/** real world latitude */	
	public final double WORLD_LATITUDE;
	/** real world longitude */
	public final double WORLD_LONGITUDE;
	/** map position */	
	public final double MAP_XPOS;
	/** real world longitude */
	public final double MAP_YPOS;
	/** ID for this node - used only internally for data store */ 
	public final int ID;
	/** class-wide counter of nodes */
	private static int nodeCount;
	/** how far west this location is - a location cannot have a trail leading to a location of lower rank, only equal or greater rank*/
	private final int RANK;
	
	private String locationName;
	private List<TrailEdge> outboundTrails;
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locationName String representing name of location
	 * @param xPos the x position for the location on the world map
	 * @param yPos the y position for the location on the world map
	 * @param Latitude Real World(tm) latitude of location (for MapScene manufacture)
	 * @param Longitude Real World(tm) longitude of location (for MapScene Manufacture)
	 * @param trails number of trails exiting this location
	 */
	public LocationNode(String locationName, int xPos, int yPos, int Latitude, int Longitude, int trails, int rank){
		this.ID = LocationNode.nodeCount++;
		//until we can get a nice source for lat and long data
		MAP_XPOS = xPos;
		MAP_YPOS = yPos;
		
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		this.outboundTrails = new ArrayList<TrailEdge>(trails);
		this.locationName = locationName;
		this.RANK = rank;
	}
	
	public LocationNode(String name, int xPos, int yPos, int trails, int rank){
		//makes unique name for location, temporarily, until we can make them prettier.
		this(name, xPos, yPos, 0, 90, trails, rank);
		
	}
	
	public LocationNode(int xPos, int yPos, int trails, int rank){
		//makes unique name for location, temporarily, until we can make them prettier.
		this("Location " + LocationNode.nodeCount ,xPos, yPos, 0, 90, trails, rank);
		
	}

	/**
	 * adds a new, leaving, {@code TrailEdge} to this {@code LocationNode}
	 * @param newTrail
	 */
	
	public void addTrail(TrailEdge newTrail){
		this.outboundTrails.add(newTrail);
	}
	/**
	 * gets the entire list of {@code outboundTrails}. Not preferred method of acces - keep datastore hidden
	 * @return the list of trails for this particular node
	 */
	public List<TrailEdge> getOutboundTrails(){
		return this.outboundTrails;
	}
	
	/**
	 * get a particular trail from the {@code outboundTrail} list by index
	 * @param index the particular trail we want
	 * @return the TrailEdge object we want
	 */
	public TrailEdge getOutBoundTrailByIndex(int index){
		return this.outboundTrails.get(index);
	}
	
	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.locationName;
	}
	
	/**
	 * method to return all instance variables easily without having to string getters
	 * only dev mode
	 * @return string of all string representations of private variables
	 */
	public String debugToString(){
		String retVal;
		int numTrails = this.outboundTrails.size();
		retVal = "Name : " + this.locationName + " X pos : " + this.MAP_XPOS + " Y pos : " + this.MAP_YPOS + "\n";
		retVal += "World Lat : " + this.WORLD_LATITUDE + " World Long : " + this.WORLD_LONGITUDE + " \n";
		retVal += "Internal ID : " + this.ID + "Total Nodes currently made : " + LocationNode.nodeCount + " \n";
		retVal += "Rank : " + this.RANK + "Total Exit Trail Count : " + numTrails + " \n";
		
		if (numTrails == 0){
			retVal += "\tNo trails implemented \n";
		}
		
		for (int i = 0; i < numTrails ;i++){
			retVal += "\tExit Trail " + i + " : " + this.outboundTrails.get(i).toString() + " \n";
		}
		
		
		return retVal;
	}
	
}//class LocationNode
