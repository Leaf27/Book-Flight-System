package cs509Blizzard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import cs509Blizzard.FBNDataStructures.Airport;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;
import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;
import cs509Blizzard.SearchEngine.SearchEngine;

/**
 * The Class FlyByNightAPI is the API of the fly by night system. The system interface should 
 * only interact with this class, and  different interface should use this same API.  
 * 
 * 
 * @author Ermal Toto
 * @version   1.0
 * @since     2015-02-01 
 */

public class FlyByNightAPI {
	
	/**  The Departing trips are a lists of departing trips to be displayed to customer. */
	private TripList DepartingTrips = null;
	
	/**  The Returning trips are a lists of returning trips to be displayed to customer. */
	private TripList ReturningTrips = null;

	/** The selected departing trip. (used to build a ticket) */
	private Trip selectedDepartingTrip = null;
	
	/** The selected returning trip. (used to build a ticket)*/
	private Trip selectedReturningTrip = null;

	/** The engine1. Is used to search departing trips. Separation allows for future optimization and possible multi-threading */
	private SearchEngine engine1 = new SearchEngine();
	
	/** The engine2. Is used to search returning trips. Separation allows for future optimization and possible multi-threading */
	private SearchEngine engine2 = new SearchEngine();

	/** True if searching for round trips, false if one-way. */
	private boolean roundTrip = false;
	
	/**  True if Trips spanning 2 days are allowed. */
	private boolean overnight = false;

	/**  The origin as a 3 letter Airport code. */
	private String origin;

	/**  The destination  as a 3 letter Airport code. */
	private String destination;

	/** The cabin class. */
	private String cabinClass;

	/** The minimum layover time in minutes. 30 minutes by default */
	private int minLayover = FBNConstants.MINLAYOVER; 
	
	/** The maximum layover time in minutes. 180 minutes by default */
	private int maxLayover = FBNConstants.MAXLAYOVER; 

	/** The maximum number of stops. */
	private int maxNumberOfStops = 2;

	/** The departure date composed of year, month and day. */
	private int departureYear,departureMonth,departureDay;
	
	/** The return date composed of year, month and day. */
	private int returnYear,returnMonth,returnDay;
	
	/** The departure filters outbound low and high. */
	private int departureFiltersOutboundlow = 0, departureFiltersOutboundhigh = 24;
	
	/** The arrival filters outbound low and high. */
	private int arrivalFiltersOutboundlow = 0, arrivalFiltersOutboundhigh = 24;
	
	/** The departure filters inbound low and high. */
	private int departureFiltersInboundlow = 0, departureFiltersInboundhigh = 24;
	
	/** The arrival filters inbound low and high. */
	private int arrivalFiltersInboundlow = 0, arrivalFiltersInboundhigh = 24;
	
	/** The verbose search functionality. When true display search logs in the console. */
	private boolean verboseSearch = true;

	/**
	 * This is a constructor that instantiates a new flybynight system. In this steps
	 * airports and aircracts are loaded into cache.
	 */
	public FlyByNightAPI()
	{
		// Initialize Aircraft List
		CachingSystem.getAircraftsToCache();
		CachingSystem.getAirportsToCache();
		this.setOvernight(overnight);
	}

	/**
	 * Gets the departing trips. This methods needs to be executing after a search in order to get the trips that 
	 * are to be displayed to the users. 
	 *
	 * @return the departing trips
	 */
	public TripList getDepartingTrips() {
		return this.DepartingTrips;
	}
	
	/**
	 * Search for (departing) trips between origin and destination. Starting from origin ending in destination. 
	 */
	public void searchDepartingTrips() {
		this.DepartingTrips = this.engine1.search();
	}
	
	/**
	 * Gets the returning trips. This methods needs to be executing after a search in order to get the trips that 
	 * are to be displayed to the users. 
	 *
	 * @return the returning trips
	 */
	public TripList getReturningTrips() {
		return this.ReturningTrips;
	}
	
	/**
	 * Search for (return) trips between destination and origin. Starting from destination ending in origin. 
	 */
	public void searchReturningTrips() {
		this.ReturningTrips = this.engine2.search();
	}
	
	/**
	 * Gets the selected departing trip.
	 *
	 * @return the selected departing trip
	 */
	public Trip getSelectedDepartingTrip() {
		return this.selectedDepartingTrip;
	}
	
	/**
	 * Sets the selected departing trip.
	 *
	 * @param selectedDepartingTrip the new selected departing trip
	 */
	public void setSelectedDepartingTrip(Trip selectedDepartingTrip) {
		this.selectedDepartingTrip = selectedDepartingTrip;
	}
	
	/**
	 * Gets the selected returning trip.
	 *
	 * @return the selected returning trip
	 */
	public Trip getSelectedReturningTrip() {
		return this.selectedReturningTrip;
	}
	
	/**
	 * Sets the selected returning trip in response to a user action in the interface (selecting the trip).
	 *
	 * @param selectedReturningTrip the new selected returning trip
	 */
	public void setSelectedReturningTrip(Trip selectedReturningTrip) {
		this.selectedReturningTrip = selectedReturningTrip;
	}
	
	/**
	 * Checks if the search to be performed is round trip, per user specification through the interface. 
	 *
	 * @return true, if is round trip, false otherwise
	 */
	public boolean isRoundTrip() {
		return this.roundTrip;
	}
	
	/**
	 * Sets the round trip switch per user specification through the interface.
	 *
	 * @param roundTrip the new round trip
	 */
	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}
	
	/**
	 * Checks if the search to be performed should include overnight flights, per user specification through the interface. 
	 *
	 * @return true, if is overnight, false otherwise
	 */
	public boolean isOvernight() {
		return this.overnight;
	}


	/**
	 * Sets the overnight switch per user specification through the interface.
	 *
	 * @param overnight the new overnight
	 */
	public void setOvernight(boolean overnight) {
		this.overnight = overnight;
		this.engine1.setIncludeOvernightFlights(overnight);
		this.engine2.setIncludeOvernightFlights(overnight);
	}

	/**
	 * Gets the origin airport (3 letter code) that is set for the current search. 
	 *
	 * @return the origin
	 */
	public String getOrigin() {
		return this.origin;
	}
	
	/**
	 * Sets the origin airport (3 letter code)  for the current search. 
	 *
	 * @param origin the new origin
	 */
	public void setOrigin(String origin) {
		this.engine1.setOrigin(origin);
		this.engine2.setDestination(origin);
		this.origin = origin;
	}
	
	/**
	 * Gets the destination  airport (3 letter code) that is set for the current search. 
	 *
	 * @return the destination
	 */
	public String getDestination() {
		return this.destination;
	}
	
	/**
	 * Sets the destination  airport (3 letter code)  for the current search. 
	 *
	 * @param destination the new destination
	 */
	public void setDestination(String destination) {
		this.engine2.setOrigin(destination);
		this.engine1.setDestination(destination);
		this.destination = destination;
	}

	/**
	 * Gets the minimum layover time in minutes that is set for the current search. 
	 *
	 * @return the minimum layover in minutes
	 */
	public int getMinLayover() {
		return this.minLayover;
	}
	
	/**
	 * Sets the minimum layover time in minutes  for the current search. 
	 *
	 * @param minLayover the new minimum layover time in minutes
	 */
	public void setMinLayover(int minLayover) {
		this.engine1.setMinConnectingTime(minLayover);
		this.engine2.setMinConnectingTime(minLayover);
		this.minLayover = minLayover;
	}

	/**
	 * Gets the maximum layover time in minutes that is set for the current search. 
	 *
	 * @return the maximum layover time in minutes
	 */
	public int getMaxLayover() {
		return maxLayover;
	}

	/**
	 * Sets the maximum layover time in minutes that is set for the current search. 
	 *
	 * @param maxLayover the new max layover time in minutes.
	 */
	public void setMaxLayover(int maxLayover) {
		this.engine1.setMaxConnectingTime(maxLayover);
		this.engine2.setMaxConnectingTime(maxLayover);
		this.maxLayover = maxLayover;
	}

	/**
	 * Sets the departure date  for the current search. 
	 *
	 * @param departureDate the new departure date
	 */
	public void setDepartureDate(Calendar departureDate) {       
		int year = departureDate.get(Calendar.YEAR);
		int month = departureDate.get(Calendar.MONTH) + 1;
		int day = departureDate.get(Calendar.DAY_OF_MONTH);
		this.engine1.setYear(year);
		this.engine1.setMonth(month);
		this.engine1.setDay(day);
		this.departureYear = year; this.departureMonth = month; this.departureDay = day;
	}



	/**
	 * Sets the return date for the current search. 
	 *
	 * @param returnDate the new return date
	 */
	public void setReturnDate(Calendar returnDate) {
		int year = returnDate.get(Calendar.YEAR);
		int month = returnDate.get(Calendar.MONTH) + 1;
		int day = returnDate.get(Calendar.DAY_OF_MONTH);
		this.engine2.setYear(year);
		this.engine2.setMonth(month);
		this.engine2.setDay(day);
		this.returnYear = year; this.returnMonth = month; this.returnDay = day;
	}

	/**
	 * Gets the cabin class that is set for the current search. 
	 *
	 * @return the cabin class
	 */
	public String getCabinClass() {
		return this.cabinClass;
	}
	
	/**
	 * Sets the cabin class for the current search. 
	 *
	 * @param cabinClass the new cabin class
	 */
	public void setCabinClass(String cabinClass) {
		this.engine1.setCabinclass(cabinClass);
		this.engine2.setCabinclass(cabinClass);
		this.cabinClass = cabinClass;
	}
	
	/**
	 * Gets the maximum number of stops  that is set for the current search. 
	 *
	 * @return the maximum number of stops to that is allowed in the search. 
	 */
	public int getMaxNumberOfStops() {
		return this.maxNumberOfStops;
	}

	/**
	 * Sets the max number of stops for the current search. 
	 *
	 * @param maxNumberOfStops the new maximum number of stops allowed in the search. 
	 */
	public void setMaxNumberOfStops(int maxNumberOfStops) {
		this.engine1.setMAXNUMSTOPS(maxNumberOfStops);
		this.engine2.setMAXNUMSTOPS(maxNumberOfStops);
		this.maxNumberOfStops = maxNumberOfStops;
	}


	/**
	 * This method is used to lock the database and connects to the @see Reservation System
	 * It is not used by the current interface, but could be used by future implementations. 
	 * 
	 */
	public void lockDatabase()
	{
		ReservationSystem.lock();
	}


	/**
	 * This method is used to unlock the database and connects to the @see ReservationSystem
	 * It is not used by the current interface, but could be used by future implementations. 
	 * 
	 */
	public void unlockDatabase()
	{
		ReservationSystem.unlock();
	}


	/**
	 * Reserve trip utilizes the  ReservationSystem to first build a trip to be purchased, than purchase it.
	 * The reason for the separation is to allow cabin swaps (each flight XML booking request is built separately then merged)
	 * as well as booking both legs of a round trip together. 
	 * 
	 * <p>
	 * 
	 * This functionality assumes that the search engine relies on search engine implementing cabin swaps. 
	 *
	 * @param outboundTrip the trip object containing all the flights in the outbound trip
	 * @param inboundTrip the trip object containing all the flights in the inbound trip
	 * @param desiredCabin the desired cabin class
	 * @return true, if all the flights in a trip were booked successfuly
	 */
	public boolean ReserveTrip(Trip outboundTrip, Trip inboundTrip, String desiredCabin)
	{

		Flight aflight;
		// A ticket is first build (XML generated for each trip outbound and inbound)
		if(outboundTrip != null)
		{
			List<Flight> flightsOutbound = outboundTrip.getListofFlight();
			for(int i = 0; i < flightsOutbound.size();i++)
			{
				aflight = flightsOutbound.get(i);
				ReservationSystem.buildTicket(aflight.flightNumber, aflight.selectedFlightClass);	
			}
		}
		if(inboundTrip != null)
		{
			List<Flight> flightsinbound = inboundTrip.getListofFlight();
			for(int i = 0; i < flightsinbound.size();i++)
			{
				aflight = flightsinbound.get(i);
				ReservationSystem.buildTicket(aflight.flightNumber, aflight.selectedFlightClass);
			}	
		}
		try {
			return ReservationSystem.buyTicket(); // Then purchased all at once. 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets an airport object given an airport code. This method can help the interface translate airport codes into full airport names
	 * or vice versa. 
	 *
	 * @param airportCode the airport code (3 letter code)
	 * @return the airport object @see Airport
	 */
	public Airport getAirport(String airportCode)
	{
		Airport airport = CachingSystem.getAirport(airportCode);
		return airport;
	}

	
	/**
	 * Gets a list of airports to be used by auto-complete in the format ABC-Airport Name where ABC is the 
	 * 3 letter code of the airport. 
	 * 
	 * @return List<String> (A list of airports for the auto-complete combo-boxes)
	 */
	public  ArrayList<String> generateAutoCompleteAiportList()
	{
        return CachingSystem.generateAutoCompleteAiportList();
	}	
	
	
	/**
	 * Sets the departure filters outbound.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setDepartureFiltersOutbound(int low, int high)
	{
		this.departureFiltersOutboundlow = low;
		this.departureFiltersOutboundhigh = high;
	}

	/**
	 * Sets the arrival filters outbound.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setArrivalFiltersOutbound(int low, int high)
	{
		this.arrivalFiltersOutboundlow = low;
		this.arrivalFiltersOutboundhigh = high;

	}	
	
	/**
	 * Sets the departure filters inbound.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setDepartureFiltersInbound(int low, int high)
	{
		this.departureFiltersInboundlow = low;
		this.departureFiltersInboundhigh = high;
	}

	/**
	 * Sets the arrival filters inbound.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setArrivalFiltersInbound(int low, int high)
	{
		this.arrivalFiltersInboundlow = low;
		this.arrivalFiltersInboundhigh = high;
	}	
	
	/**
	 * Filter departing by time constrains.
	 *
	 * @return the list
	 */
	public TripList filterDepartingByTimeConstrains()
	{
		TripList subsetOfDepartingTrips = new TripList();
		if(this.DepartingTrips != null)
		{
			this.DepartingTrips.setDepartureFilters(this.departureFiltersOutboundlow,this.departureFiltersOutboundhigh);
			this.DepartingTrips.setArrivalFilters(this.arrivalFiltersOutboundlow,this.arrivalFiltersOutboundhigh);	
			List<Trip> listOfTrips = this.DepartingTrips.filterByTime();
			for(int i = 0; i < listOfTrips.size(); i++)
			{
				subsetOfDepartingTrips.addTrip(listOfTrips.get(i));
			}
		}
		return subsetOfDepartingTrips;	
	}
	
	/**
	 * Filter returning by time constrains.
	 *
	 * @return the list
	 */
	public TripList filterReturningByTimeConstrains()
	{
		TripList subsetOfReturningTrips = new TripList();
		if(this.ReturningTrips != null)
		{
			this.ReturningTrips.setDepartureFilters(this.departureFiltersInboundlow,this.departureFiltersInboundhigh);
			this.ReturningTrips.setArrivalFilters(this.arrivalFiltersInboundlow,this.arrivalFiltersInboundhigh);	
			List<Trip> listOfTrips = this.ReturningTrips.filterByTime();
			for(int i = 0; i < listOfTrips.size(); i++)
			{
				subsetOfReturningTrips.addTrip(listOfTrips.get(i));
			}
		}
			
		return subsetOfReturningTrips;					
	}
	
	/**
	 * Filter departing by stops.
	 *
	 * @param stops the stops
	 * @return the list
	 */
	public TripList filterDepartingByStops(int stops)
	{
		TripList subsetOfDepartingTrips = new TripList();
		if(this.DepartingTrips != null)
		{
			List<Trip> listOfTrips = this.DepartingTrips.filterByStops(stops);
			for(int i = 0; i < listOfTrips.size(); i++)
			{
				subsetOfDepartingTrips.addTrip(listOfTrips.get(i));
			}
		}
		return subsetOfDepartingTrips;					
	}
	
	/**
	 * Filter returning by stops.
	 *
	 * @param stops the stops
	 * @return the list
	 */
	public TripList filterReturningByStops(int stops)
	{
		TripList subsetOfReturningTrips = new TripList();
		if(this.ReturningTrips != null)
		{
			List<Trip> listOfTrips = this.ReturningTrips.filterByStops(stops);
			for(int i = 0; i < listOfTrips.size(); i++)
			{
				subsetOfReturningTrips.addTrip(listOfTrips.get(i));
			}
		}
		return subsetOfReturningTrips;					
	}	
	
	
	/**
	 * Sets the caching.
	 *
	 * @param cachingIsOn the new caching
	 */
	public void setCaching(boolean cachingIsOn)
	{
		CachingSystem.setCaching(cachingIsOn);
	}
	
	/**
	 * Reset db.
	 */
	public void resetDB()
	{
		ReservationSystem.resetDB();
	}
	
	/**
	 * Unlock Database (Legacy Function for API).
	 * 
	 * @deprecated
	 */
	public void unlock()
	{
		ReservationSystem.unlock();
	}

	/**
	 * Lock Database (Legacy Function for API).
	 * 
	 * @deprecated
	 */
	public void lock()
	{
		ReservationSystem.lock();
	}
	
	/**
	 * Gets the engine1 (Departure flights engine) this is used for testing purposes.
	 *
	 * @return the engine1
	 */
	public SearchEngine getEngine1() {
		return engine1;
	}

	/**
	 * Sets the engine1. (Departure flights engine) this is used for testing purposes. In tests a new engine can be instantiated and inserted in the API class to test Engine methods.
	 *
	 * @param engine1 the new engine1
	 */
	public void setEngine1(SearchEngine engine1) {
		this.engine1 = engine1;
	}

	/**
	 * Gets the engine2. (Return flights engine) this is used for testing purposes
	 *
	 * @return the engine2
	 */
	public SearchEngine getEngine2() {
		return engine2;
	}

	/**
	 * Sets the engine2.  (Return flights engine) this is used for testing purposes. In tests a new engine can be instantiated and inserted in the API class to test Engine methods. 
	 *
	 * @param engine2 the new engine2
	 */
	public void setEngine2(SearchEngine engine2) {
		this.engine2 = engine2;
	}

	/**
	 * Checks if is verbose search is enabled.
	 *
	 * @return true, if is verbose search
	 */
	public boolean isVerboseSearch() {
		return verboseSearch;
	}

	/**
	 * Sets the verbose search for each of the search engines.
	 *
	 * @param verboseSearch the new verbose search
	 */
	public void setVerboseSearch(boolean verboseSearch) {
		this.verboseSearch = verboseSearch;
		this.engine1.setVerboseSearch(verboseSearch);
		this.engine2.setVerboseSearch(verboseSearch);

	}	
}


