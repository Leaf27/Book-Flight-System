package cs509Blizzard.FBNUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
// TODO: Auto-generated Javadoc

/* https://maps.googleapis.com/maps/api/timezone/json?location=39.6034810,-119.6822510&timestamp=1331161200&key=AIzaSyBumcQWCRe6-yeSx9E8pv17ovwMjzs1bkY
 * {
  	   "dstOffset" : 0,
  	   "rawOffset" : -28800,
  	   "status" : "OK",
  	   "timeZoneId" : "America/Los_Angeles",
  	   "timeZoneName" : "Pacific Standard Time"
   } 
 */

/**
 * The Class Timezone connects to the Googles API to determine the local times.
 */
public class Timezone {
	
	/** The Constant mUrlBase. */
	private static final String mUrlBase = "https://maps.googleapis.com/maps/api/timezone/json?";

	/**
	 * Gets the time zone from google.
	 *
	 * @param query the query
	 * @return the time zone from google
	 */
	private static String getTimeZoneFromGoogle(String query){
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		String line;
		StringBuffer result = new StringBuffer();
		try{
			url = new URL(query);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			if((responseCode >= 200)&& (responseCode<=299)){
				InputStream inputStream = connection.getInputStream();
				String encoding =connection.getContentEncoding();
				encoding=(encoding == null ? "UTF-8": encoding);
				reader= new BufferedReader(new InputStreamReader(inputStream));
				while((line=reader.readLine())!= null){
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
			return result.toString(); 
	}
	
	/**
	 * Gets the time object.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @return the time object
	 */
	public static String getTimeObject(double latitude, double longitude)
	{
		String query = (new StringBuilder(mUrlBase)).append("location=").append(latitude).append(",")
				.append(longitude).append("&timestamp=").append("0").append("&key=AIzaSyBumcQWCRe6-yeSx9E8pv17ovwMjzs1bkY").toString();
		String timeZoneObject = getTimeZoneFromGoogle(query);
		//System.out.println(query);
		return timeZoneObject;
	}
}



