/*
 * 
 */
package cs509Blizzard.FBNDataStructures;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.FBNUtilities.ComparableTime;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;



// TODO: Auto-generated Javadoc
/**
 * This class stores one trip which contains a set of flights.
 * Automatically maintains properties of trip such as departure time and arrival time.
 *
 * @author Shirui Zhang
 * @version 1.0
 * @since 2015-03-10
 */

/**
 * The Class Trip.
 */
public class Trip implements Cloneable {
	
	/** The total cost. */
	private int totalCost;
	
	/** The stops. */
	private int stops;
	
	/** The departure time. */
	private long departureTime;
	
	/** The arrival time. */
	private long arrivalTime;
	
	/** The trip details. */
	private String tripDetails; 
	
	/** The local departure time. */
	private String localDepartureTime;
	
	/** The local arrival time. */
	private String localArrivalTime;
	
	/** The list of flights. */
	private List<Flight> listofFlight;
	
	/**  The Cabin swap flag. */
	private String cabinSwapFlag = FBNConstants.NO;
	
	/** The trip duration. */
	private long tripDuration;
	
	/**
	 * Instantiates a new trip.
	 * For debug use only.
	 */
	public Trip(){
		this.listofFlight = new ArrayList<Flight>();
		this.totalCost = 0;
		this.departureTime = 0;
		this.arrivalTime = 0;
		this.stops = -1;
	}
	
	/**
	 * Instantiates a new trip.
	 *
	 * @param totalCost the total cost
	 * @param departureTime the departure time
	 * @param arrivalTime the arrival time
	 * @param stops the stops
	 */
	public Trip(int totalCost,int departureTime,int arrivalTime,int stops){
		//for test purposes only
		this.listofFlight = new ArrayList<Flight>();
		this.totalCost = totalCost;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.tripDuration = this.arrivalTime-this.departureTime;
		this.stops = stops;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString(){
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		result.append("Trip Data:" + NEW_LINE);
		result.append(" Total Cost:\t\t\t" + this.getTotalCost()  + "\t\t");
		result.append("Total Stops:\t" + this.getStops() + NEW_LINE);
		result.append(" Departure Time:\t" + this.getDepartureTime() + "\t\t");
		result.append("Arrival Time:\t" + this.getArrivalTime() + "\t");
		result.append("Travel Time:\t" + this.getTime() + NEW_LINE);
		
		
		return result.toString();
	}
	/*Constrains: 
	 * 1. If flight B is listed after Flight A, 
	 * the departure of flight B should be after the arrival of flight A. 
	 * 
	 * 2. No cyclic trips are allowed (i.e. airports: A B C D A E, ABCD forms a cycle)
	 */
	
	/**
	 * Gets the total cost of this trip.
	 * 
	 * @return the total cost
	 */
	public int getTotalCost(){
		return this.totalCost;
	}
	
	/**
	 * Gets the departure time.
	 *
	 * @return the departure time
	 */
	public long getDepartureTime(){
		return this.departureTime;
	}
	
	/**
	 * Gets the arrival time.
	 *
	 * @return the arrival time
	 */
	public long getArrivalTime(){
		return this.arrivalTime;
	}
	
	/**
	 * Gets the stops.
	 *
	 * @return the stops
	 */
	public int getStops(){
		return this.stops;
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime(){
		return this.arrivalTime - this.departureTime;
	}
	
	/**
	 * public boolean addFlight(Flight FlightToAdd, String Cabin)
	 * <p>
	 * Add a flight to the end of this trip.
	 *
	 * @param FlightToAdd The data of flight.
	 * @param Cabin String The class of cabin ("Coach", "First")
	 * @return true, if successful
	 */
	public boolean addFlight(Flight FlightToAdd){
		if (FlightToAdd.departureTime<this.arrivalTime){
			return false;
		}
		if (this.departureTime == 0){
			this.departureTime = FlightToAdd.departureTime;
			this.localDepartureTime = FlightToAdd.localDepartureTime;
		}
		if (this.arrivalTime<FlightToAdd.arrivalTime){
			this.arrivalTime = FlightToAdd.arrivalTime;
			this.localArrivalTime = FlightToAdd.localArrivalTime;

		}
		this.stops += 1;
		this.listofFlight.add(FlightToAdd);
		return true;
	}
	
	/**
	 * Gets the list of flight.
	 *
	 * @return the list of flight
	 */
	public List<Flight> getListofFlight(){
		return this.listofFlight;
	}
	
	/**
	 * Gets the trip details.
	 *
	 * @return the trip details
	 */
	public String getTripDetails() {
		return tripDetails;
	}
	
	/**
	 * Sets the trip details.
	 *
	 * @param tripDetails the new trip details
	 */
	public void setTripDetails(String tripDetails) {
		this.tripDetails = tripDetails;
	}
	
	/**
	 * Gets the local departure time.
	 *
	 * @return the local departure time
	 */
	public String getLocalDepartureTime() {
		return localDepartureTime;
	}
	
	/**
	 * Sets the local departure time.
	 *
	 * @param localDepartureTime the new local departure time
	 */
	public void setLocalDepartureTime(String localDepartureTime) {
		this.localDepartureTime = localDepartureTime;
	}
	
	/**
	 * Gets the local arrival time.
	 *
	 * @return the local arrival time
	 */
	public String getLocalArrivalTime() {
		return localArrivalTime;
	}
	
	/**
	 * Sets the local arrival time.
	 *
	 * @param localArrivalTime the new local arrival time
	 */
	public void setLocalArrivalTime(String localArrivalTime) {
		this.localArrivalTime = localArrivalTime;
	}
	
	/**
	 * Sets the local arrival time.
	 *
	 * @param tripCost the new trip cost
	 */
	public void setTripCost(double tripCost) {
		this.totalCost = (int) tripCost;
	}

	/**
	 * get the trip duration.
	 *
	 * @return the trip duration
	 */
	public long getTripDuration() {		
		return  this.tripDuration;
	}

	/**
	 * get the trip duration in days hour min and seconds.
	 *
	 * @return the formated trip duration
	 */	
	public ComparableTime getObjectTripDuration () {
		return new ComparableTime(this.tripDuration);
	}
	

	/**
	 * Calculate the trip duration.
	 */
	public void calculateTripDuration() {
		this.tripDuration = this.arrivalTime - this.departureTime;
	}

	/**
	 * Gets the cabin swap flag.
	 *
	 * @return the cabin swap flag
	 */
	public String getCabinSwapFlag() {
		return cabinSwapFlag;
	}

	/**
	 * Sets the cabin swap flag.
	 *
	 * @param cabinSwapFlag the new cabin swap flag
	 */
	public void setCabinSwapFlag(String cabinSwapFlag) {
		this.cabinSwapFlag = cabinSwapFlag;
	}	
	
}
