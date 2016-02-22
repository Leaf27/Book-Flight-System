package cs509Blizzard.FBNDataStructures;


/**
 * The Class Airport contains the necessary information to define a flight.
 * 
 * @author Huan Ye <hye@wpi.edu>
 * @version 1.0
 * @since 2015-3-15
 *
 */
public class Airport {	
	  
  	/** The airport code consisted of three capital letter, unique among airports. */
  	public String code;
	  
  	/** The airport name. */
  	public String name;
	  
  	/** The latitude of this airport */
  	public double latitude;
	  
  	/** The longitude of this airport. */
  	public double longtitude;
	  
  	/** The time zone data of the location of this airport. */
  	public String timeZoneData;
	  
	  /**
  	 * Instantiates a new airport.
  	 *
  	 * @param code           the code of an airport
  	 * @param name           the name of an airport
  	 * @param latitude       the latitude of an airport
  	 * @param longtitude     the longitude of an airport
  	 * @param timeZoneData   the time zone data
  	 */
  	public Airport(String code, String name,double latitude,double longtitude,String timeZoneData){
		  this.code= code;
		  this.name=name;
		  this.latitude=latitude;
		  this.longtitude= longtitude;
		  this.timeZoneData=timeZoneData;
	  }
}
