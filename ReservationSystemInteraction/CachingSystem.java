package cs509Blizzard.ReservationSystemInteraction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import cs509Blizzard.FBNDataStructures.Aircraft;
import cs509Blizzard.FBNDataStructures.Airport;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.FBNUtilities.FlightListKey;

/**
 * The Class CachingSystem is an intermediary between the reservation and all the other classes.
 * It it serves a two fold purpose:
 * 
 * 1.  It makes sure that no data requests task the server unless there
 * is a chance the could be being stale, or this is a request for new data. All other requests are served from the cache.
 * 
 * 2.  It simplifies interaction with the Reservation system, by adding an additional logic layer.
 * 
 *  @author    Ermal Toto <toto@wpi.edu>
 *  @version   1.0
 *  @since     2015-02-01 
 *  @see 	   ReservationSystem
 */



public  class CachingSystem {
	
	/** The system flight cache. Contains a hash of all flights that have been used in the current search idexed by origin and data @see FlightListKey */
	private static HashMap<FlightListKey, List<Flight>> systemCache = new HashMap<FlightListKey, List<Flight>>();
	
	/** The airplane list contains a hash of all  @see Aircraft models and their characteristics  indexed by model */
	private static HashMap<String, Aircraft> airplaneList = new HashMap<String, Aircraft>();
	
	/** The Aiport lis contains a list of Airports @see Airport indexed by the 3 letter airport code */
	private static HashMap<String, Airport> aiportList = new HashMap<String, Airport>();

	/** The caching switch turns the caching functionality on or off. Performance is greatly reduced when the caching is off */
	private static boolean caching = true;	
	
	/** The verbose switch turns the caching engine debug functionality on or off.  */
	private static boolean verbose = false;	
	private static int airportCount = 0;
	private static int airportCountNoCache = 0;
    
	// Given a airport code, a year, month and day get all flights from that airport on that day (GMT) and the subsequent day.
	/**
	 * This method return a list of flights from an airport specified in the origin as a 3 letter code at a given date specified by 
	 * year, month and day. It also retrieves the flights for the subsequent day. If the flights are found in the cache @see systemCache it retreives
	 * them from the systemCache, otherwise it pulls the flights from the ReservationSystem
	 *
	 * @param origin is the 3 letter airport code for the airport of origin for which flights have to be retrived
	 * @param year is the year of the date for which flights have to be retrived
	 * @param month is the month of the date  for which flights have to be retrived
	 * @param day is the day of the date  for which flights have to be retrived
	 * @return the flights from the specified airport and date as a list of Flights @see Flight
	 */
	public static List<Flight> getFlights(String origin,int year, int month, int day)
	{
		FlightListKey key = new FlightListKey(origin,year,month,day);
		//String key = "KEY" + origin + year + month + day;
		List<Flight> flightsDayTwo = null;
		List<Flight> flights = null;
		List<Flight> flightsYester = null;

		
		if(CachingSystem.aiportList.size() == 0)
			CachingSystem.getAirportsToCache();
		
		if(CachingSystem.airplaneList.size() == 0)
			CachingSystem.getAircraftsToCache();

		if(CachingSystem.systemCache.containsKey(key) && CachingSystem.isCaching())
		{
			if(CachingSystem.verbose) System.out.println("Cached Result");
			flightsYester =  CachingSystem.systemCache.get(key); // If information is cached retreive it internally from the cache
		}
		else { // Otherwise retreive 2 days worth of flights from the reservation system, and cache them
			if(CachingSystem.verbose) System.out.println("DB Result");			
	        Calendar dayOne = Calendar.getInstance();
	        dayOne.set(year,month - 1, day); 
	        dayOne.add(Calendar.DATE,1);
	        int yearNext = dayOne.get(Calendar.YEAR);
	        int monthNext = dayOne.get(Calendar.MONTH) + 1;
	        int dayNext = dayOne.get(Calendar.DAY_OF_MONTH);
	        dayOne.add(Calendar.DATE,-2);
	        int yearPrevious = dayOne.get(Calendar.YEAR);
	        int monthPrevious = dayOne.get(Calendar.MONTH) + 1;
	        int dayPrevious = dayOne.get(Calendar.DAY_OF_MONTH);	 
	        
			try {
				flightsYester = (List<Flight>) ReservationSystem.getDeparture(origin, yearPrevious, monthPrevious, dayPrevious);
				flights = (List<Flight>) ReservationSystem.getDeparture(origin, year, month, day);
				flightsDayTwo = (List<Flight>)  ReservationSystem.getDeparture(origin, yearNext, monthNext, dayNext);
				if(flights != null) flightsYester.addAll(flights);
				if(flightsDayTwo != null) flightsYester.addAll(flightsDayTwo);
				if(flightsYester != null) CachingSystem.systemCache.put(key,flightsYester);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//CachingSystem.airportCount++;
		}
		//System.out.println("With Cache: " + airportCount + "With out Cache: " + CachingSystem.airportCountNoCache++ );

		return flightsYester;						
	}
	
	/**
	 * Gets the list of aircrafts into the caching system as a hash indexed by Aircraft model. Each object is an Aircraft (@see Aircraft).
	 * This method is run by the FlyByNight API class (@see FlyByNight on initialization)
	 *
	 * @return void (aircrafts are  stored in cache)
	 */
	public static void getAircraftsToCache()
	{
		try {
			CachingSystem.airplaneList = ReservationSystem.getAirplanes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the list of airports into the caching system as a hash indexed by airport code. Each object is an Airport (@see Airport).
	 * This method is run by the FlyByNight API class (@see FlyByNight on initialization)
	 *
	 * @return void (airports are  stored in cache)
	 */
	public static void getAirportsToCache()
	{
		try {
			CachingSystem.aiportList = XMLParser.getAirportStatic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	/**
	 * Gets a list of airports to be used by auto-complete in the format ABC-Airport Name where ABC is the 
	 * 3 letter code of the airport. 
	 * 
	 * @return List<String> (A list of airports for the auto-complete combo-boxes)
	 */
	public static ArrayList<String> generateAutoCompleteAiportList()
	{
		ArrayList<String> airportAutoList = new ArrayList<String>();
		Set<String> keys = CachingSystem.aiportList.keySet();

        for(String key: keys){
            airportAutoList.add(key + " - " + CachingSystem.aiportList.get(key).name.toUpperCase());
        }
        return airportAutoList;
	}	
	
	/**
	 * Given a flight and a cabin class this method getss the number of available seats on the that flight for the given cabin class. 
	 *
	 * @param flight the flight number
	 * @param cabinClass the cabin class
	 * @return the available seats on that flight and cabin class
	 */
	public static int getAvailableSeats(Flight flight,String cabinClass)
	{
		Aircraft aircraft = CachingSystem.airplaneList.get(flight.airplaneModel);
		if(cabinClass.equals(FBNConstants.COACH))
			return aircraft.CoachSeats - flight.coachClassSeats;
		else
			return aircraft.FirstClassSeats - flight.firstClassSeats;
	}
	
	/**
	 * Gets the airport as an Airport object (@see Airport) given an aiport code
	 *
	 * @param airportCode the 3 letter airport code
	 * @return the airport as an Airport object (@see Airport)
	 */
	public static Airport getAirport(String airportCode)
	{
		Airport airport = CachingSystem.aiportList.get(airportCode);
		return airport;
	}

	/**
	 * Gets the aircraft as an Aircraft object (@see Aircraft) given an aircraft model
	 *
	 * @param aircraft model 
	 * @return the aircraft as an Aircraft object (@see Aircraft)
	 */
	public static Aircraft getAircraft(String aircraftModel)
	{
		Aircraft aircraft = CachingSystem.airplaneList.get(aircraftModel);
		return aircraft;
	}
	
	/**
	 * Clears the cache. This method is invoked when there is a chance that the data might be stale. 
	 */
	public static void clear()
	{
		CachingSystem.systemCache.clear();
	}
	
	/**
	 * Checks if is caching is set to true. This method is never used in production, but is implemented for development purposes. 
	 *
	 * @return true, if is caching
	 */
	public static boolean isCaching() {
		return CachingSystem.caching;
	}

	/**
	 * getTimeZoneOffset method returns the time offset for an airport given that airport's 3 letter code
	 * 

	 * @param airportCode the airport code for the airport that is related to the time stamp. Used to get the conversion timezone. 
	 * @return the time offset in milliseconds
	 */
	public static long getTimeZoneOffset(String airportCode)
	{
		return TimeZone.getTimeZone(CachingSystem.getAirport(airportCode).timeZoneData).getOffset(0);
	}
	
	
	/**
	 * Set caching to true or false. If caching is set to false the system is significantly slower. This method
	 * is used only to troubleshoot system issues, and perform performance measures. 
	 *
	 * @param caching the new caching argument as a boolean variable. 
	 */
	public static  void setCaching(boolean caching) {
		CachingSystem.caching = caching;
	}
	
	/**
	 * Set verbose debugging to true or false. 
	 * 
	 * @param verbose the new verbose argument as a boolean variable. 
	 */
	public static  void setVerbose(boolean verbose) {
		CachingSystem.verbose = verbose;
	}	
}
