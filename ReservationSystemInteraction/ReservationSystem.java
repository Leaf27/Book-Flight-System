package cs509Blizzard.ReservationSystemInteraction;




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import cs509Blizzard.FBNDataStructures.Aircraft;
import cs509Blizzard.FBNDataStructures.Airport;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.FBNUtilities.FlyByNightPop;


/**
 * The Class ReservationSystem, which is used to get information from database and post parameter to database.
 * @author Huan Ye <hye@wpi.edu>
 * @version 1.0
 * @since 2015-3-6
 *
 */
public class ReservationSystem {

	/** The Constant mUrlBase. */
	private static final String mUrlBase = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";

	/** The current ticket. */
	private static String currentTicket = "";

	/**
	 * Gets the information of airport from database and stores the data into hashmap type.
	 *
	 * @return the airports
	 * @throws Exception if disconnected 
	 */
	public static HashMap<String, Airport> getAirports () throws Exception{
		String query = QueryFactory.getAirports();
		String airportINFO= readDataFromDB(query);
		return XMLParser.getAirport(airportINFO);
	}



	/**
	 * Gets the information of departure from database and stores the data into an arraylist.
	 *
	 * @param airport     the airport
	 * @param yyyy        the year
	 * @param mm          the month
	 * @param dd          the day
	 * @return the data of all departing flight for a given airport and for a given date
	 * @throws Exception if disconnected
	 */
	public static List<Flight> getDeparture (String airport, int yyyy, int mm, int dd) throws Exception{
		String query = QueryFactory.getDeparture(airport, yyyy, mm, dd);
		String DBInfo= readDataFromDB(query);
		return XMLParser.parseDeparture(DBInfo);
	}

	/**
	 * Reset DB.
	 *
	 * @return null
	 */
	public static String resetDB (){
		String query=QueryFactory.resetDB();
		return readDataFromDB(query);		
	}

	/**
	 * Gets the information of aircraft from database and stores the data into hashmap type.
	 *
	 * @return the information for all aircraft
	 * @throws Exception if disconnected
	 */
	public static HashMap<String, Aircraft> getAirplanes() throws Exception{
		String query=QueryFactory.getAirplanes();
		String airCraftINFO= readDataFromDB(query);
		return XMLParser.getAirCraft(airCraftINFO);
	}

	/**
	 * Read data from DB.If the connection does not work, pop out "Unable to connect to server".It is used to get information about aircrafts, airports, departure flights and arriving flights.
	 *
	 * @param query the query
	 * @return string from URL
	 */
	public static String readDataFromDB(String query){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		try{
			url = new URL(mUrlBase + query);
			connection = (HttpURLConnection) url.openConnection();			
			connection.setRequestMethod("GET");			
			Integer responseCode = null;

			try{
				responseCode = connection.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK){
					throw new IOException("Unable to connect to server");
				}	
			}	
			catch(IOException c){
				FlyByNightPop.errorBox("Unable to connect to server", "Connection Error");
				c.getMessage();
				return null;
			}	

			//System.out.println("readDataFromDB Response code: " + responseCode);
			if((responseCode >= 200)&& (responseCode<=299)){
				InputStream inputStream = connection.getInputStream();
				String encoding =connection.getContentEncoding();
				encoding=(encoding == null ? "UTF-8": encoding);	
				reader= new BufferedReader(new InputStreamReader(inputStream,encoding));
				while((line=reader.readLine())!= null){	
					//PrintStream out = new PrintStream(System.out, true, "UTF-8");
				    //out.println(line);
					result.append(line);
				}			
				reader.close();
			}	
		}catch(IOException e){
			e.printStackTrace();
		}	
		catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(result.toString());
		return result.toString(); 
	}


	//lock DB and unlock DB and buy tickets
	/**
	 * Lock DB.
	 *
	 * @return true, if successful
	 */
	public static boolean lock(){
		String post = QueryFactory.lock();
		return postAction(post);
	}

	/**
	 * Unlock.
	 *
	 * @return true, if successful
	 */
	public static boolean unlock(){
		String post = QueryFactory.unlock();
		return postAction(post);
	}

	/**
	 * For each ticket needed to be reserved, combine the flight number and cabin class into a string based on the desired format.
	 *
	 * @param flight   the flight number
	 * @param cabin    the cabin class
	 */
	public static void buildTicket(String flight,String cabin)
	{
		if(cabin.equals(FBNConstants.COACH)){
			ReservationSystem.currentTicket +=  "<Flight number=\""+flight+"\" seating=\"Coach\" />";
		}
		else if (cabin.equals(FBNConstants.FIRST)){
			ReservationSystem.currentTicket +=  "<Flight number=\""+flight+"\" seating=\"FirstClass\" />";
		}		
	}

	/**
	 * build a string containing flight number and cabin class by combining a string 'currentTicket' built from function buildTicket, 
	 * with key characters . Then set the string as parameter used for buying ticket post action.
	 *
	 * @return true, if successful
	 * @throws Exception  if disconnected or the flight is full or database was locked by other teams.
	 */
	public static boolean buyTicket()throws Exception{
		boolean postSuccessful = false;
		ReservationSystem.lock(); 
		String flightData="null";
		flightData = "<Flights>" + ReservationSystem.currentTicket + "</Flights>";
		//System.out.println("flights:"+flightData);
		String query = QueryFactory.buyTicket(flightData);
		ReservationSystem.currentTicket = "";
		postSuccessful = postAction(query);
		ReservationSystem.unlock();
		return postSuccessful;
	}


	/**
	 * Post action.
	 * <p>
	 * This method is used to do the post action for locking DB, buying tickets and unlocking DB. When the connection does not work, 
	 * pop out"can not connect to server". If the database is locked by other team, pop out"Can not book a flight now, please try again".
	 * If one flight is available when the customer is searching but unavailable when he decides to reserve it, 
	 * pop out "The flight is full".
	 * <p>
	 * 
	 * @param post    the parameter for post action
	 * @return true, if successful
	 * @throws Exception if disconnected or the flight is full or database was locked by other teams.
	 */
	public static boolean  postAction(String post) {
		URL url;
		HttpURLConnection connection;
		try{
			url= new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();		
			String params=post;
			connection.setDoOutput(true);
			connection.setDoInput(true);				
			Integer responseCode = null;
			try{
				DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
				writer.writeBytes(params);
				writer.flush();
				writer.close();				
				responseCode = connection.getResponseCode();
				System.out.println("postAction Response Code: " + responseCode);							
			}
			catch (IOException e){
				FlyByNightPop.errorBox("can not connect to server", "Connection Error");
				e.getMessage();
				return false;

			}

			try{
				if(responseCode == HttpURLConnection.HTTP_PRECON_FAILED){
					throw new Exception("Can not book a flight now, please try again");	
				}
			}
			catch(Exception e){
				FlyByNightPop.errorBox("Can not book a flight now, please try again", "Booking Error");
				e.getMessage();
				return false;

			}	

			try{
				if(responseCode == HttpURLConnection.HTTP_NOT_MODIFIED){
					throw new Exception();	
				}
			}
			catch(Exception e){
				FlyByNightPop.errorBox("The flight is full", "Booking Error");
				e.getMessage();
				return false;
			}

			if((responseCode>=200)&&(responseCode<=299)){
				BufferedReader in = new BufferedReader (new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while((line = in.readLine())!=null){
					response.append(line);
				}
				in.close();		
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
