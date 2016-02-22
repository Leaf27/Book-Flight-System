package cs509Blizzard.ReservationSystemInteraction;

/**
 *  A combination for all Queries needed for getting airport data, airplane data, departure flight data for a given date and a given airport, 
 * arriving flight data for a given date and for a given airport, reset database system, lock database system, unlock database system and buy tickets.
 * 
 * @author Huan Ye
 * @version 1.0
 * @since 2015-3-6
 *
 */
public class QueryFactory {
	 
 	/**
 	 * create the query for getting airport.
 	 *
 	 * @return  the query for getting airport
 	 */
 	public static String getAirports(){
		 return "?team=TeamBlizzard&action=list&list_type=airports";
	 }
	 
 	
 	
 	/**
 	 * create the query for getting airplanes.
 	 *
 	 * @return  the query for getting airplanes
 	 */
 	public static String getAirplanes(){
		 return "?team=TeamBlizzard&action=list&list_type=airplanes";
	 }
 	
 	
 	
     
     /**
      * create the query for getting all departing flights for a given airport and for a given day.
 	  *
      *
      * @param airport     the airport code, which can be used to look up an airport object @see Airport and get additional information
      * @param yyyy        the year, four digits
      * @param mm          the month, two digits
      * @param dd          the day, two digits
      * @return the query for getting departing airplanes
      */
 	
 	
 	
     public static String getDeparture(String airport, int yyyy, int mm, int dd){
    	 return "?team=TeamBlizzard&action=list&list_type=departing&airport=" + airport +"&day=" + yyyy + "_" + mm + "_" + dd;
     }
     
     
     
     /**
      * create the query for getting all arriving flights for a given airport and for a given day.
 	  *
       * @param airport     the airport code, which can be used to look up an airport object @see Airport and get additional information
      * @param yyyy        the year, four digits
      * @param mm          the month, two digits
      * @param dd          the day, two digits
      * @return the query for getting arriving airplanes
      */
     public static String getArriving(String airport, int yyyy, int mm, int dd){
    	 return "?team=TeamBlizzard&action=list&list_type=arriving&airport=" + airport +"&day=" + yyyy + "_" + mm + "_" + dd;
     }
     
     
     
     
     /**
      * Reset db.
      *
      * @return the parameter for reseting DB
      */
     public static String resetDB(){
    	 return"?team=TeamBlizzard&action=resetDB";
     }
     
     
     /**
      * Lock DB
      *
      * @return the parameter for locking DB
      */
     public static String lock(){
    	 return "team=TeamBlizzard&action=lockDB";
     }
     
     /**
      * Unlock DB.
      *
      * @return the parameter for unlocking DB
      */
     public static String unlock(){
    	 return "team=TeamBlizzard&action=unlockDB";
     }
    
     /**
      * Buy all desired tickets .
      *
      * @param XMLFILE    the xmlfile containing flight data consisted of flight number and cabin class.
      * @return the parameter for buying ticket.
      */
     public static String buyTicket(String XMLFILE){
    	 return "team=TeamBlizzard&action=buyTickets&flightData="+XMLFILE;
     }
}
