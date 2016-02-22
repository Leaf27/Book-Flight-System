package cs509Blizzard.FBNDataStructures;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;


// TODO: Auto-generated Javadoc
/**
 * The Class Flight contains the necessary information to define a flight. 
 * 
 * @author Ermal Toto <toto@wpi.edu>
 * @author Huan Ye <hye@wpi.edu>
 */
public class Flight {
	
	/**  The airplane model, which can be used to look up an Airplane object @see Airplane and get additional information. */
	public String airplaneModel;    
	
	/** The flight time. Can also be deduced by substracting departure time from arrival time */
	public double flightTime;       
	
	/** The flight number. Is a unique number identifying the flight */
	public String flightNumber;     
	
	/** The departure code as a 3 letter airport code. */
	public String departureCode;    
	
	/** The arrival code as a 3 letter airport code. */
	public String arrivalCode;     
	
	/** The departure time in milliseconds from the epoch (timestamp). */
	public long departureTime;       
	
	/**  The arrival time in milliseconds from the epoch (timestamp). */
	public long arrivalTime;      
	
	/** The local departure time. */
	public String localDepartureTime; 
	
	/** The local arrival time. */
	public String localArrivalTime; 	
	
	/** The number of booked first class seats. */
	public int firstClassSeats;    
	
	/** The number of booked coach class seats. */
	public int coachClassSeats;    
	
	/** The first class price per seat. */
	public double firstClassPrice; 
	
	/** The coach class price per seat. */
	public double coachClassPrice;  
	
	/**  The flight class determined from the engine based on user preferences and availability, coach by default. */
	public String selectedFlightClass = FBNConstants.COACH;

	// Flight Constructor
	/**
	 * Instantiates a new flight.
	 *
	 * @param departureCode the departure code
	 * @param departureTime the departure time
	 * @param arrivalCode the arrival code
	 * @param arrivalTime the arrival time
	 * @param airplaneModel the airplane model
	 * @param flightTime the flight time
	 * @param flightNumber the flight number
	 * @param firstClassSeats the number of first class seats
	 * @param coachClassSeats the number of coach class seats
	 * @param firstClassPrice the first class price per seat
	 * @param coachClassPrice the coach class price per seat
	 */
	public Flight(String departureCode,long departureTime,String arrivalCode,long arrivalTime,String airplaneModel,double flightTime,String flightNumber,int firstClassSeats,int coachClassSeats,double firstClassPrice,double coachClassPrice)
	{
		this.airplaneModel = airplaneModel;   
		this.flightTime = flightTime;    	
		this.flightNumber = flightNumber;     
		this.departureCode = departureCode;   
		this.arrivalCode = arrivalCode;     
		this.departureTime = departureTime;      
		this.arrivalTime = arrivalTime;       
		this.localArrivalTime = this.localTimeConverter(arrivalTime, arrivalCode);
		this.localDepartureTime = this.localTimeConverter(departureTime, departureCode);
		//First Class
		this.firstClassSeats = firstClassSeats;
		this.firstClassPrice = firstClassPrice;		
		//Coach Class
		this.coachClassPrice = coachClassPrice;		
		this.coachClassSeats = coachClassSeats;   

	}
	
	/**
	 * Gets the price of the flight based on the cabin class selection
	 *
	 * @return the price
	 */
	public double getPrice()
	{
		double price = -1;
		if(this.selectedFlightClass == FBNConstants.COACH)
			price =  this.coachClassPrice;
		else if(this.selectedFlightClass == FBNConstants.FIRST)
			price = this.firstClassPrice;
		return price;
	}
	
	/**
	 * Local time converter function  generates a local time given an airportCode and a timestamp.
	 * UTC timestamps are still retained and used for all search engine functions	
	 *
	 * @param timestamp the timestamp to be converted
	 * @param airportCode the airport code for the airport that is related to the time stamp. Used to get the conversion timezone. 
	 * @return the converted local time at the airport formated as: MMMM dd HH:mm z
	 */
	private String localTimeConverter(long timestamp, String airportCode)
	{
		Date date = null;
		String localTime = "";
		SimpleDateFormat f = new SimpleDateFormat(FBNConstants.LOCAL_DATE_FORMAT);
		f.setTimeZone(TimeZone.getTimeZone(CachingSystem.getAirport(airportCode).timeZoneData));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		localTime = f.format(cal.getTime());
		return localTime;

	}
}

