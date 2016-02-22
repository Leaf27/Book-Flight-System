package cs509Blizzard.SearchEngine;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.FBNUtilities.FBNUtilities;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;

/** 
 * The Class SearchEngine performs a recursive search to find
 * flights between 2 airports. Flights from each airport are 
 * pulled. If the destination is found the current recursion trail
 * is recognized as a trip and placed in a trip list. If the destination
 * is not found, flights from each of the flights pulled are recursively
 * checked and so on, until all possible routes to a destination are found.
 * All searches are subject to hard limitations by time schedule (you can not
 * arrive before you depart etc) and soft limitations such as number of stops,
 * 
 *  @author    Ermal Toto <toto@wpi.edu>
 *  @version   1.0
 *  @since     2015-02-01 
 */

public class SearchEngine {

	/** The maxnumstops. This is normally a number between 0 and 3, but higher numbers are used for testing*/
	private  int MAXNUMSTOPS;

	/** The origin. This is a 3 letter code identifying the airport of departure */
	private  String origin;

	/** The destination.  This is a 3 letter code identifying the airport of arrival*/
	private  String destination;

	/** The cabinclass. This is a string that identifies one of two cabin classes: coach or first class*/
	private  String cabinclass;

	/** The min connecting time is the  minimum layover time in minutes, by default set to 30min*/
	private  long minConnectingTime;

	/** The max connecting time is the maximum layovber time in minutes, by default set to 3h (180min) */
	private  long maxConnectingTime;

	/** The include overnight flights is a boolean switch that determines if overnight flights are searched for*/
	private  boolean includeOvernightFlights = false;

	/** The year of the date of the flight */
	private int year;

	/** The month of the date of the flight  */
	private int month;

	/** The day of the date of the flight  */
	private int day;
	
	/** The verbose search switch. if enabled shows search trace. */
	private boolean verboseSearch = true;


	/**
	 * Search flight  is a recursive method that does a criteria based exhaustive search.
	 * It performs a depth first search to find a Path from origin to destination 
	 * within a limited number of stops, and taking in account time constrains such as 
	 * minimum layover time, maximum layover time, overnight flights, flight order, and seat
	 * availability.
	 *
	 * @param listOfTrips is the list of trips accumulated by all successful recursion trails (that end in a destination).
	 * @param tripObject the trip object currently being constructed by all the flights in a recursion trail. It will be added to the trip list if it ends in a desired destination.
	 * @param parent  the previous flight passed down in a recursive call
	 * @param origin the 3 letter airport code for the origin airport
	 * @param trip is a string describing a trip list being build by a recursion strand. 
	 * @param price the price of the trip which is subject to change during the recursion (incremental)
	 * @param depth the depth of the recursion call. Makes sure we don't get a stack overflow and controls the number of stops
	 * @return the trip list containing all the possible trips from origin to destination. 
	 */
	private  TripList searchFlight(TripList listOfTrips, Trip tripObject, Flight parent,String origin, String trip, double price, int depth)
	{	
		boolean consequitiveFlights = true;
		boolean availableSeats = true;
		boolean overnightCheck = false;

		double flightPrice = 0;
		double tripPrice = price;
		long connectionTime = 0;
		Trip currentTrip = null;
		String tempTrip = "";
		String cabinClass = this.getCabinclass();
		String swapWarning = "";
		String connectingTime = "";

		if(this.getMAXNUMSTOPS(depth))
		{					

			List<Flight> flights = CachingSystem.getFlights(origin,this.getYear(),this.getMonth(),this.getDay());

			if(flights != null)
				for (int i = 0; i < flights.size(); i++)
				{
					availableSeats = true;
					consequitiveFlights = true;
					flightPrice = 0;
					connectionTime = 0;
					tripPrice = price;
					swapWarning = "";
					connectingTime = "";



					try {
						if(tripObject == null)
							currentTrip = new Trip();
						else
							currentTrip = (Trip) tripObject.clone();						
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					Flight aflight = flights.get(i);	
					aflight.selectedFlightClass = this.getCabinclass();
					// filter flights that are on previous local date
					if((FBNUtilities.getDayFromString(aflight.localDepartureTime) == this.getDay()) || depth > 1)
					{
						if(parent != null) 
						{
							// Make sure connection time complies with customer request
							connectionTime = aflight.departureTime - parent.arrivalTime;
							if((connectionTime >= this.getMinConnectingTime()) && (connectionTime <= this.getMaxConnectingTime()))
								consequitiveFlights = true;
							else
								consequitiveFlights = false;
						}

						// Make sure there are enough seats in the customers preferred class
						// Need to modify to offer alternatives in case there are available seats 
						// in another cabin class.
						if(this.getCabinclass() == FBNConstants.COACH) { 
							if(CachingSystem.getAvailableSeats(aflight, FBNConstants.COACH) <= 0) 
							{	
								availableSeats = false;
								if(CachingSystem.getAvailableSeats(aflight, FBNConstants.FIRST) > 0) 
								{
									aflight.selectedFlightClass = FBNConstants.FIRST;
									availableSeats = true;
								}
							}
						} else if(CachingSystem.getAvailableSeats(aflight, FBNConstants.FIRST) <= 0) 
						{
							availableSeats = false;
							if(CachingSystem.getAvailableSeats(aflight, FBNConstants.COACH) > 0) 
							{
								availableSeats = true;
								aflight.selectedFlightClass = FBNConstants.COACH;
							}
						}

						if(!this.isIncludeOvernightFlights())
						{
							if(FBNUtilities.getDayFromString(aflight.localDepartureTime) != FBNUtilities.getDayFromString(aflight.localArrivalTime))
								overnightCheck = false;
							else
								overnightCheck = true;
						} else overnightCheck = true;

						// Only consider flights that  have acceptable connection times and available seats
						if(consequitiveFlights && availableSeats  && overnightCheck) {
							flightPrice = aflight.getPrice();	
							tripPrice += flightPrice; 
							cabinClass = aflight.selectedFlightClass;;

							if(cabinClass!= this.getCabinclass()) { 
								swapWarning = "!!! Cabin Class Change !!!";
								currentTrip.setCabinSwapFlag(FBNConstants.YES);
							}
							
							if(depth > 1) connectingTime = "Layover Time: " + connectionTime/FBNConstants.MSINMINUTE / 60 + "hours and " + connectionTime/FBNConstants.MSINMINUTE % 60 + "minutes";
							currentTrip.addFlight(aflight);
							tempTrip = trip + "\n -------------------- " + connectingTime +
									"\nDeparture Airport: " + aflight.departureCode + " : (" + CachingSystem.getAirport(aflight.departureCode).name + ")" +
									"\tLocal Time at Departure: " + aflight.localDepartureTime +
									"\nArrival Airport: " + aflight.arrivalCode + " : (" + CachingSystem.getAirport(aflight.arrivalCode).name + ")" +
									"\tLocal Time at Destination: " + aflight.localArrivalTime +
									"\nFlight Time: " + aflight.flightTime/FBNConstants.MSINMINUTE + "min" +   
									"\tPrice for this segment:  $" + flightPrice + 
									"\tFlightNumber: " + aflight.flightNumber + 
									"\tCabin Class: " + aflight.selectedFlightClass + " " + swapWarning + 
									"\tAvailable Seats: " +  CachingSystem.getAvailableSeats(aflight, aflight.selectedFlightClass);

							if(aflight.arrivalCode.equals(this.getDestination()))
							{	
								currentTrip.setTripCost( Math.round(tripPrice * 100.0)/100.0);
								currentTrip.calculateTripDuration();
								currentTrip.setTripDetails(tempTrip + "\n ----- Total Cost: $" + Math.round(tripPrice * 100.0)/100.0 + " USD --- Trip Duration: " + currentTrip.getObjectTripDuration());
								if(this.isVerboseSearch()) System.out.println(currentTrip.getTripDetails());
								listOfTrips.addTrip(currentTrip);
							}
							else if(!trip.contains(aflight.arrivalCode)) // No Repeats, needs to be changed to an object search ..... DANGER!!!!!
							{
								searchFlight(listOfTrips, currentTrip,aflight,aflight.arrivalCode,tempTrip,tripPrice, depth + 1);							

							}
						}
					}
				}
		}
		return listOfTrips;
	}	



	/**
	 * Search is an encapsulation of the searchFlight method (@see searchFlight). It takes no parameters and returns a trip list object
	 * This method uses class variables to set up a new search. 
	 * 
	 * @return the trip list containing all the trips found by searchFlight
	 */
	public  TripList search()
	{
		CachingSystem.clear();
		return searchFlight(new TripList(),null,null,this.getOrigin(),this.getOrigin(),0,1);
	}


	/**
	 * Gets the 3 letter origin airport currently set in the search engine.
	 *
	 * @return the origin as a string containing the origin airport code
	 */
	public  String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin (3 letter airport code) for the flight search. 
	 *
	 * @param origin the new origin for the search 
	 */
	public  void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Gets the cabin class currently set in the search engine.
	 *
	 * @return the cabin class currently set in the search engine
	 */
	public  String getCabinclass() {
		return this.cabinclass;
	}

	/**
	 * Sets the cabinclass for a new search to be performed.
	 *
	 * @param cabinclass the new cabin class (First or Coach)
	 */
	public  void setCabinclass(String cabinclass) {
		this.cabinclass = cabinclass;
	}

	/**
	 * Gets the destination for a new search to be performed.
	 *
	 * @return the destination of the current search (3 letter airport code)
	 */
	public  String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination for a new search to be performed.
	 *
	 * @param destination the new destination
	 */
	public  void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * This method returns true if the  maximum recursion depth has been reached. 
	 * This is  related to the maximum number of stops: maxnumstops + 2
	 * 
	 * @param depth is the number of stops to be searched.
	 * @return true if depth is greater then maxnumstops  + 2 false otherwise
	 */
	public  boolean getMAXNUMSTOPS(int depth) {
		return (depth < (MAXNUMSTOPS + FBNConstants.BASEDEPTH));
	}

	/**
	 * Sets the maxnumstops which is the maximum number of stops to be searched (flight legs)
	 *
	 * @param mAXNUMSTOPS the new maxnumstops
	 */
	public  void setMAXNUMSTOPS(int mAXNUMSTOPS) {
		MAXNUMSTOPS = mAXNUMSTOPS;
	}

	/**
	 * Gets the minimum connecting time between flights (in minutes). 
	 *
	 * @return the min connecting time in minutes
	 */
	public  long getMinConnectingTime() {
		return minConnectingTime;
	}

	/**
	 * Sets the minimum connecting time between flights (in minutes). 
	 *
	 * @param minConnectingTime the new min connecting time in minutes.
	 */
	public  void setMinConnectingTime(long minConnectingTime) {
		this.minConnectingTime = minConnectingTime * FBNConstants.MSINMINUTE;
	}

	/**
	 * Gets the maximum connecting time between flights (in minutes). 
	 *
	 * @return the maximum connecting time in minutes.
	 */
	public  long getMaxConnectingTime() {
		return maxConnectingTime;
	}

	/**
	 * Sets the maximum connecting time between flights (in minutes)
	 *
	 * @param maxConnectingTime the new max connecting time in minutes.
	 */
	public  void setMaxConnectingTime(long maxConnectingTime) {
		this.maxConnectingTime = maxConnectingTime * FBNConstants.MSINMINUTE;
	}	

	/**
	 * Checks if search should include overnight flights.
	 *
	 * @return true, if set to include overnight flights
	 */
	public  boolean isIncludeOvernightFlights() {
		return includeOvernightFlights;
	}

	/**
	 * Sets if the search should include overnight flights.
	 *
	 * @param includeOvernightFlights the new include overnight flights (true/false)
	 */
	public  void setIncludeOvernightFlights(boolean includeOvernightFlights) {
		this.includeOvernightFlights = includeOvernightFlights;
	}	

	/**
	 * Gets the year that is set for the current search. .
	 *
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year for the current search.
	 *
	 * @param year the new year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Gets the month that is set for the current search.
	 *
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Sets the month for the current search.
	 *
	 * @param month the new month
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * Gets the day that is set for the current search.
	 *
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Sets the day for the current search. .
	 *
	 * @param day the new day
	 */
	public void setDay(int day) {
		this.day = day;
	}



	public boolean isVerboseSearch() {
		return verboseSearch;
	}



	public void setVerboseSearch(boolean verboseSearch) {
		this.verboseSearch = verboseSearch;
	}
}


