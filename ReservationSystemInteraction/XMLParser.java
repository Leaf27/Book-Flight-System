package cs509Blizzard.ReservationSystemInteraction;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cs509Blizzard.FBNDataStructures.Aircraft;
import cs509Blizzard.FBNDataStructures.Airport;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNUtilities.Timezone;

/**
 * The Class XMLParser.
 *
 * @author Huan Ye <hye@wpi.edu>
 * @version 1.0
 * @since 2015-3-6
 *
 */
public class XMLParser {

	/**
	 * The parameter consists of flight information. The method is used for parsing the parameter which is stored in XML format and return a List of Flights with information extracted correctly.
	 *
	 *
	 * @param DBInfo     a string containing flight information
	 * @return  an arraylist of all flights departed from a given airport and for a given day.
	 * @throws Exception throws exception if the DBInfo is null or containing no flight information.
	 */
	public static List<Flight> parseDeparture( String DBInfo) throws Exception {
		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		if(DBInfo!=null&&DBInfo.length()>70){
			Document document = convertStringToDocument(DBInfo);

			List<Flight> flightList = new ArrayList<>();

			//get all departure node.
			NodeList nodeList_departure = document.getElementsByTagName("Departure");
			String departureCode = nodeList_departure.item(0).getFirstChild().getTextContent();
			/**
			 * Sample XML file
	    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
	    <Flights>
	    	<Flight Airplane="A340" FlightTime="137" Number="1781">
	    		<Departure>
	    			<Code>BOS</Code>
	    			<Time>2015 May 09 20:07 EDT</Time>
	    		</Departure>
	    		<Arrival>
	    			<Code>MEM</Code>
	    			<Time>2015 May 09 22:24 EDT</Time>
	    		</Arrival>
	    		<Seating>
	    			<FirstClass Price="$323.25">28</FirstClass
	    			><Coach Price="$38.60">23</Coach>
	    		</Seating>
	    	</Flight>
	    </Flights>
			 */
			for (int i = 0; i < nodeList_departure.getLength(); i++) {


				//get all other information of specific flight
				Node node_flight=nodeList_departure.item(i).getParentNode();
				Node node_arrival=nodeList_departure.item(i).getNextSibling();
				Node node_Seating= node_flight.getLastChild();

				// flight info
				String airplaneModel = node_flight.getAttributes().getNamedItem("Airplane").getNodeValue();
				double flightTime = Integer.valueOf(node_flight.getAttributes().getNamedItem("FlightTime").getNodeValue())*60*1000; // milliseconds
				String flightNumber = node_flight.getAttributes().getNamedItem("Number").getNodeValue();

				// departure
				String departureTimeString = nodeList_departure.item(i).getLastChild().getTextContent();
				long departureTime = milliseconds(departureTimeString); 

				// arrival
				String arrivalCode = node_arrival.getFirstChild().getTextContent();
				String arrivalTimeString = node_arrival.getLastChild().getTextContent();
				long arrivalTime = milliseconds(arrivalTimeString);

				NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

				// first class
				String firstClassPrice = node_Seating.getFirstChild().getAttributes().getNamedItem("Price").getNodeValue();
				double firstClassPriceValue = format.parse(firstClassPrice.substring(firstClassPrice.indexOf("$") + 1)).doubleValue();
				int firstClassSeats = Integer.valueOf(node_Seating.getFirstChild().getTextContent());

				// coach class
				String coachClassPrice = node_Seating.getLastChild().getAttributes().getNamedItem("Price").getNodeValue();
				double coachClassPriceValue = format.parse(coachClassPrice.substring(coachClassPrice.indexOf("$") + 1)).doubleValue();
				int coachClassSeats = Integer.valueOf(node_Seating.getLastChild().getTextContent());

				// create Flight object, add to list
				flightList.add(new Flight( departureCode, departureTime,arrivalCode,arrivalTime,airplaneModel, 
						flightTime, flightNumber, firstClassSeats, coachClassSeats,firstClassPriceValue, 
						coachClassPriceValue));
			}



			return flightList;
		}
		else {return new ArrayList<>();}
	}

	/**
	 * Convert date to milliseconds.
	 *
	 * @param date      the date in the format of yyyy-mm-dd
	 * @return long 
	 * @throws Exception the exception
	 */
	private static long milliseconds(String  date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm zzz", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("EDT"));
		Date millis= sdf.parse(date);
		return millis.getTime();
	}

	/**
	 * Convert string to document.
	 *
	 * @param xmlStr   the xml string
	 * @return the document
	 */
	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		try 
		{  
			builder = factory.newDocumentBuilder();  
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
			return doc;
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		return null;
	}


	/*get aircraft*/	
	/**
	 * Parse the string which contains aircraft information and stores the data into hashmap type.
	 *
	 * @return the hashmap of aircrafts
	 * @throws Exception  if disconnected
	 */
	public static  HashMap<String, Aircraft> getAirCraft(String airCraftINFO)throws Exception{
		if(airCraftINFO!=null){		   
			Document document = convertStringToDocument(airCraftINFO);	        
			List<Aircraft> airCraft = new ArrayList<>();		
			NodeList nodeList_airCraft = document.getElementsByTagName("Airplane");
			for(int i=0;i<nodeList_airCraft.getLength();i++){
				String Manufacturer=nodeList_airCraft.item(i).getAttributes().getNamedItem("Manufacturer").getNodeValue();
				String Model=nodeList_airCraft.item(i).getAttributes().getNamedItem("Model").getTextContent();
				int FirstClassSeats=Integer.valueOf(nodeList_airCraft.item(i).getFirstChild().getTextContent());
				int CoachSeats=Integer.valueOf(nodeList_airCraft.item(i).getLastChild().getTextContent());
				airCraft.add(new Aircraft(Manufacturer,Model,FirstClassSeats,CoachSeats));
			}
			HashMap<String, Aircraft> airCraftMap = new HashMap<String, Aircraft>();
			for (int i=0;i<airCraft.size(); i++) {
				airCraftMap.put(airCraft.get(i).Model, airCraft.get(i));
			}
			return airCraftMap;
		}
		else {return new HashMap<String, Aircraft>();}
	}	

	/**
	 * Parse the string which contains airport information and stores the data into hashmap type.
	 *
	 * @return the hashmap of airports
	 * @throws Exception if disconnected
	 */
	public static  HashMap<String, Airport> getAirport(String airportINFO)throws Exception{
		if(airportINFO!=null){
			Document document = convertStringToDocument(airportINFO);	        
			List<Airport> airPort = new ArrayList<>();		
			NodeList nodeList_airPort = document.getElementsByTagName("Airport");
			for(int i=0;i<nodeList_airPort.getLength();i++){
				String code=nodeList_airPort.item(i).getAttributes().getNamedItem("Code").getNodeValue();
				String name=nodeList_airPort.item(i).getAttributes().getNamedItem("Name").getTextContent();
				double latitude=Double.valueOf(nodeList_airPort.item(i).getFirstChild().getTextContent());
				double longtitude=Double.valueOf(nodeList_airPort.item(i).getLastChild().getTextContent());
				String timeZoneInfo =Timezone.getTimeObject(latitude, longtitude);			
				String timeZone = "";
				try {
					JSONObject timeZoneObject = new JSONObject(timeZoneInfo);
					timeZone = timeZoneObject.get("timeZoneId").toString();
//					System.out.println(timeZone);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					timeZone = "failedToPullTimeZone";
					e.printStackTrace();
				}		
				//System.out.println("airPort.add(new Airport(\"" + code + "\",\"" + name + "\"," + latitude + "," + longtitude + ",\"" + timeZone + "\"));");

				airPort.add(new Airport(code,name,latitude,longtitude,timeZone));
			}

			HashMap<String, Airport> airPortMap = new HashMap<String, Airport>();
			for (int i=0;i<airPort.size(); i++) {
				airPortMap.put(airPort.get(i).code, airPort.get(i));
			}
			return airPortMap;
		}
		else{return new HashMap<String, Airport>();}
	}	

	// E.T: This is a static implementation of the function above. 
	// It is a work around google api limitations and speed issues.
	/**
	 * Gets the airport static.
	 *
	 * @return the airport static
	 * @throws Exception the exception
	 */
	public static  HashMap<String, Airport> getAirportStatic()throws Exception{
		List<Airport> airPort = new ArrayList<>();		

		airPort.add(new Airport("ATL","Hartsfield-Jackson Atlanta International",33.641045,-84.427764,"America/New_York"));
		airPort.add(new Airport("ANC","Ted Stevens Anchorage International Airport",61.176033,-149.990079,"America/Anchorage"));
		airPort.add(new Airport("AUS","Austin-Bergstrom International",30.197703,-97.666327,"America/Chicago"));
		airPort.add(new Airport("BWI","Baltimore/Washington International",39.177641,-76.668446,"America/New_York"));
		airPort.add(new Airport("BOS","Logan International",42.365855,-71.009624,"America/New_York"));
		airPort.add(new Airport("CLT","Charlotte Douglas International",35.214688,-80.947369,"America/New_York"));
		airPort.add(new Airport("MDW","Chicago Midway Airport",41.787028,-87.752156,"America/Chicago"));
		airPort.add(new Airport("ORD","Chicago O'Hare International",41.974397,-87.907321,"America/Chicago"));
		airPort.add(new Airport("CVG","Cincinnati/Northern Kentucky International",39.053529,-84.66307,"America/New_York"));
		airPort.add(new Airport("CLE","Cleveland Hopkins International",41.412663,-81.847981,"America/New_York"));
		airPort.add(new Airport("CMH","Port Columbus International",40.000158,-82.887198,"America/New_York"));
		airPort.add(new Airport("DFW","Dallas/Ft. Worth International - DFW Airport",32.900075,-97.040378,"America/Chicago"));
		airPort.add(new Airport("DEN","Denver International Airport",39.856372,-104.67377,"America/Denver"));
		airPort.add(new Airport("DTW","Detroit Metropolitan Wayne County Airport",42.216446,-83.355427,"America/New_York"));
		airPort.add(new Airport("FLL","Fort Lauderdale/Hollywood International",26.074499,-80.150548,"America/New_York"));
		airPort.add(new Airport("RSW","Southwest Florida International",26.534055,-81.75534,"America/New_York"));
		airPort.add(new Airport("BDL","Bradley International",41.939117,-72.686063,"America/New_York"));
		airPort.add(new Airport("HNL","Hawaii Honolulu International",21.324808,-157.925181,"Pacific/Honolulu"));
		airPort.add(new Airport("IAH","George Bush Intercontinental",29.990494,-95.336858,"America/Chicago"));
		airPort.add(new Airport("HOU","William P. Hobby Airport",29.654345,-95.276646,"America/Chicago"));
		airPort.add(new Airport("IND","Indianapolis International",39.717102,-86.295616,"America/New_York"));
		airPort.add(new Airport("MCI","Kansas City International",39.300888,-94.712691,"America/Chicago"));
		airPort.add(new Airport("LAS","McCarran International",36.084256,-115.153803,"America/Los_Angeles"));
		airPort.add(new Airport("LAX","Los Angeles International",33.944432,-118.408359,"America/Los_Angeles"));
		airPort.add(new Airport("MEM","Memphis International",35.042345,-89.979216,"America/Chicago"));
		airPort.add(new Airport("MIA","Miami International Airport",25.796131,-80.287014,"America/New_York"));
		airPort.add(new Airport("MSP","Minneapolis/St. Paul International",44.885002,-93.222296,"America/Chicago"));
		airPort.add(new Airport("BNA","Nashville International",36.126599,-86.677414,"America/Chicago"));
		airPort.add(new Airport("MSY","Louis Armstrong International",29.992457,-90.258979,"America/Chicago"));
		airPort.add(new Airport("JFK","John F. Kennedy International",40.641519,-73.77816,"America/New_York"));
		airPort.add(new Airport("LGA","LaGuardia International",40.777183,-73.873955,"America/New_York"));
		airPort.add(new Airport("EWR","Newark Liberty International",40.689722,-74.174537,"America/New_York"));
		airPort.add(new Airport("OAK","Metropolitan Oakland International",37.712811,-122.219797,"America/Los_Angeles"));
		airPort.add(new Airport("ONT","Ontario International",34.056205,-117.598046,"America/Los_Angeles"));
		airPort.add(new Airport("MCO","Orlando International",28.431427,-81.308104,"America/New_York"));
		airPort.add(new Airport("PHL","Philadelphia International",39.874581,-75.242423,"America/New_York"));
		airPort.add(new Airport("PHX","Sky Harbor International",33.437551,-112.007799,"America/Phoenix"));
		airPort.add(new Airport("PIT","Pittsburgh International",40.496029,-80.241311,"America/New_York"));
		airPort.add(new Airport("PDX","Portland International",45.58999,-122.595115,"America/Los_Angeles"));
		airPort.add(new Airport("RDU","Raleigh-Durham International",35.880301,-78.787953,"America/New_York"));
		airPort.add(new Airport("SMF","Sacramento International",38.69534,-121.590086,"America/Los_Angeles"));
		airPort.add(new Airport("SLC","Salt Lake City International",40.790139,-111.979114,"America/Denver"));
		airPort.add(new Airport("SAT","San Antonio International",29.531407,-98.468412,"America/Chicago"));
		airPort.add(new Airport("SAN","Lindbergh Field International",32.734013,-117.193304,"America/Los_Angeles"));
		airPort.add(new Airport("SFO","San Francisco International",37.621598,-122.37903,"America/Los_Angeles"));
		airPort.add(new Airport("SJC","Mineta San Jose International",37.364199,-121.929023,"America/Los_Angeles"));
		airPort.add(new Airport("SNA","John Wayne Airport, Orange County",33.676427,-117.867519,"America/Los_Angeles"));
		airPort.add(new Airport("SEA","Seattle-Tacoma International",47.450464,-122.308816,"America/Los_Angeles"));
		airPort.add(new Airport("STL","Lambert-St. Louis International",38.749969,-90.372176,"America/Chicago"));
		airPort.add(new Airport("TPA","Tampa International",27.983757,-82.537132,"America/New_York"));
		airPort.add(new Airport("IAD","Dulles International Airport",38.953379,-77.456571,"America/New_York"));
		airPort.add(new Airport("DCA","Ronald Reagan Washington National",38.851463,-77.040242,"America/New_York"));

		HashMap<String, Airport> airPortMap = new HashMap<String, Airport>();
		for (int i=0;i<airPort.size(); i++) {
			airPortMap.put(airPort.get(i).code, airPort.get(i));
		}
		return airPortMap;
	}	

}



