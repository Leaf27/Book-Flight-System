package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

/**
 * These are unit tests for the  Class TripList.
 * 
 * @author TeamBlizzard
 */
public class TripListTest {

	/** The list. */
	public TripList list = null;
	//public Flight(String departureCode,long departureTime,String arrivalCode,long arrivalTime,String airplaneModel,
	//	double flightTime,String flightNumber,int firstClassSeats,int coachClassSeats,double firstClassPrice,double coachClassPrice)
	/**
	 * Sets the up.
	 */
	@Before	
	public void setUp(){
		CachingSystem.getAirportsToCache();
		CachingSystem.getAircraftsToCache();
		final long B0030 = 1431232200000l;
		final long B0500 = 1431248400000l;
		final long L1200 = 1431284400000l;
		final long L2200 = 1431320400000l;
		final long L1700 = 1431302400000l;
		final long N1200 = 1431273600000l;
		//1431189000 May 09, 16:30 UTC, May 10, 00:30 EST
		//
		f1 = new Flight("BOS",B0030,"SFO",L1200,"A320",L1200-B0030,"TEST1",10,20,110,100);
		f2 = new Flight("BOS",B0500,"SFO",L2200,"A320",L2200-B0500,"TEST2",10,20,220,200);
		f3 = new Flight("BOS",B0030,"JFK",N1200,"A320",N1200-B0030,"TEST3",10,20,330,300);
		f4 = new Flight("JFK",N1200,"SFO",L1700,"A320",L1700-N1200,"TEST4",10,20,440,400);
		
		list = new TripList();
		trip1 = new Trip();
		trip1.addFlight(f1);
		trip1.setTripCost(110);
		trip2 = new Trip();
		trip2.addFlight(f2);
		trip2.setTripCost(220);
		trip3 = new Trip();
		trip3.addFlight(f3);
		trip3.addFlight(f4);
		trip3.setTripCost(730);
		list.addTrip(trip1);
		list.addTrip(trip2);
		list.addTrip(trip3);
	}
	
	/** The f1. */
	public Flight f1 = null;
	
	/** The f2. */
	public Flight f2 = null;
	
	/** The f3. */
	public Flight f3 = null;
	
	/** The f4. */
	public Flight f4 = null;	
	
	/** The trip1. */
	public Trip trip1 = null;
	
	/** The trip2. */
	public Trip trip2 = null;
	
	/** The trip3. */
	public Trip trip3 = null;
	
	/**
	 * Test add trip.
	 */
	@Test
	public void testAddTrip() {
		List<Trip> expected = list.getTrips();
		Trip test = new Trip();
		expected.add(new Trip());
		list.addTrip(test);
		assertEquals("Set and Get Origin", expected.toString(), list.getTrips().toString());
	}

	/**
	 * Test get trip.
	 */
	@Test
	public void testGetTrip() {
		Trip expected = trip1;
		Trip actual = list.getTrip(0);
		assertEquals("Set and Get Origin", expected, actual);
	}
//
//	@Test
//	public void testTripList() {
//		fail("Not yet implemented");
//	}

	/**
 * Test get trips.
 */
@Test
	public void testGetTrips() {
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(trip1);
		expected.add(trip2);
		expected.add(trip3);
		List<Trip> actual = list.getTrips();
		assertEquals("Set and Get Origin", expected, actual);
	}

	/**
	 * Test sort by price.
	 */
	@Test
	public void testSortByPrice() {		
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(trip1);
		expected.add(trip2);
		expected.add(trip3);
		assertEquals("Set and Get Origin", expected, list.sortByPrice());
	}

	/**
	 * Test sort by duration.
	 */
	@Test
	public void testSortByDuration() {
		list = new TripList();
		
		list.addTrip(new Trip(100,50,100,2));		
		list.addTrip(new Trip(200,100,160,3));
		list.addTrip(new Trip(150,75,115,4));
		
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(new Trip(150,75,115,4));
		expected.add(new Trip(100,50,100,2));
		expected.add(new Trip(200,100,160,3));
		assertEquals("Set and Get Origin", expected.toString(), list.sortByDuration().toString());
	}

	/**
	 * Test filter by stops.
	 */
	@Test
	public void testFilterByStops() {
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(trip3);
		List<Trip> actual = list.filterByStops(1);
		assertEquals("Set and Get Origin", expected.toString(), actual.toString());
	}

	/**
	 * Test filter by time.
	 */
	@Test
	public void testFilterByTime() {
		list.setDepartureFilters(0, 10);
		list.setArrivalFilters(11, 13);
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(trip1);
		assertEquals("Set and Get Origin", expected.toString(), list.filterByTime().toString());
	}

//	@Test
//	public void testOutput() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testSetDepartureFilters() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetArrivalFilters() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testMain() {
//		fail("Not yet implemented");
//	}

}
