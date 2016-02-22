/*
 * 
 */
package cs509Blizzard.FBNDataStructures;
import java.util.*;

import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNUtilities.FBNUtilities;

// TODO: Auto-generated Javadoc
/**
 * This class stores a list of trips. It connects between search engine and interface.
 * It also calculate stops and total cost automatically.
 *
 * @author Shirui Zhang
 * @version 1.0
 * @since 2015-03-10
 */


public class TripList {
	
	/** The List of trips. */
	private List<Trip> ListofTrip = new ArrayList<Trip>();
	
	/** The departure low. */
	private int departureLow = 0;	
	
	/** The departure high. */
	private int departureHigh = 24;
	
	/** The arrival low. */
	private int arrivalLow = 0;
	
	/** The arrival high. */
	private int arrivalHigh = 24;


	
	/**
	 * Adds the trip.
	 *
	 * @param T the trip going to be added
	 * @return true, if successful
	 */
	public boolean addTrip(Trip T){
		return this.ListofTrip.add(T);		
	}
	
	// 
	/**
	 * Get a single trip from trip list.
	 *
	 * @param i the id of trip.
	 * @return the trip
	 */
	public Trip getTrip(int i)
	{
		return this.ListofTrip.get(i);
	}
	
	
	/**
	 * Instantiates an empty trip list.
	 */
	public TripList(){
		
	}
	
	/**
	 * Gets all the trips.
	 *
	 * @return the trips
	 */
	public List<Trip> getTrips(){
		List<Trip> result = new ArrayList<Trip>();
		result.addAll(this.ListofTrip);
		return result;
	}
	
	/**
	 * Sort by price.
	 *
	 * @return the sorted list of trips
	 */
	public List<Trip> sortByPrice(){
		Collections.sort(this.ListofTrip, new Comparator<Trip>(){
			public int compare(Trip T1, Trip T2){
				if (T1.getTotalCost()<T2.getTotalCost()){
					return -1;
				}else if (T1.getTotalCost()==T2.getTotalCost()){
					return 0;
				}else{
					return 1;
				}
			}
		});
		return this.ListofTrip;
	}
	
	/**
	 * Sort by duration.
	 *
	 * @return the sorted list of trips
	 */
	public List<Trip> sortByDuration(){
		Collections.sort(this.ListofTrip, new Comparator<Trip>(){
			public int compare(Trip T1, Trip T2){
				if (T1.getTripDuration()<T2.getTripDuration()){
					return -1;
				}else if (T1.getTripDuration()==T2.getTripDuration()){
					return 0;
				}else{
					return 1;
				}
			}
		});
		return this.ListofTrip;
	}	
	
	/**
	 * Filter by stops.
	 *
	 * @param stops max stops
	 * @return the list
	 */
	public List<Trip> filterByStops(int stops){
		List<Trip> result = new ArrayList<Trip>();
		for (Trip item:this.ListofTrip){
			if (item.getStops()==stops){
				result.add(item);
			}
		}
		return result;
	}
	
	/**
	 * Filter by time.
	 * @return the list of trips filtered based on departure and arrival limits preivously set. 
	 */
	public List<Trip> filterByTime(){
		List<Trip> result = new ArrayList<Trip>();
		for (Trip item:this.ListofTrip){
			int localDepartureHour = FBNUtilities.getHourFromString(item.getLocalDepartureTime());
			int localArrivalHour   = FBNUtilities.getHourFromString(item.getLocalArrivalTime());
//			System.out.println(item.toString());
//			System.out.println(item.getLocalDepartureTime());
//			System.out.println(item.getLocalArrivalTime());
//			System.out.println(localDepartureHour);
//			System.out.println(localArrivalHour);
			if ((localDepartureHour <= this.departureHigh) &&
				(localDepartureHour >= this.departureLow) &&
				(localArrivalHour   <= this.arrivalHigh) &&
				(localArrivalHour   >= this.arrivalLow))
			{
				result.add(item);
			}
		}
		return result;
	}
	
	/**
	 * Output information about trips in console.
	 * For debug use.
	 *
	 * @param LT the trip list needed to be outputed.
	 */
	public static void output(List<Trip> LT){
		for (Iterator<Trip> itr = LT.iterator();itr.hasNext();){
			Trip item = itr.next();
			System.out.println(item.toString());
		}
	}
	
	/**
	 * Sets the departure filters.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setDepartureFilters(int low, int high)
	{
		this.departureLow = low;
		this.departureHigh = high;
	}

	/**
	 * Sets the arrival filters.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setArrivalFilters(int low, int high)
	{
		this.arrivalLow = low;
		this.arrivalHigh = high;
	}	
	
	/**
	 * The main method.
	 * For test purpose only.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		TripList TL = new TripList();
		TL.ListofTrip.add(new Trip(100,50,100,2));
		TL.ListofTrip.add(new Trip(200,100,150,3));
		TL.ListofTrip.add(new Trip(150,75,125,4));
//		output(TL.sortByPrice());
		output(TL.filterByStops(3));
		
	}

}
