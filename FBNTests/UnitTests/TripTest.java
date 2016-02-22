package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;

import java.util.*;

/**
 * These are unit tests for the Trip Class. 
 * 
 * @author TeamBlizzard
 */
public class TripTest {
	
	/** The trip. */
	private Trip trip = null;
	
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
	 * Test get total cost.
	 */
	@Test
	public void testGetTotalCost() {
		int expected = 730;
		assertEquals("Transfer trip to string", expected, trip3.getTotalCost());
	}

	/**
	 * Test get departure time.
	 */
	@Test
	public void testGetDepartureTime() {
		long expected = 1431232200000l;
		assertEquals("Transfer trip to string", expected, trip1.getDepartureTime());
	}

	/**
	 * Test get arrival time.
	 */
	@Test
	public void testGetArrivalTime() {
		long expected = 1431284400000l;
		assertEquals("Transfer trip to string", expected, trip1.getArrivalTime());
	}

	/**
	 * Test get stops.
	 */
	@Test
	public void testGetStops() {
		int expected = 1;
		assertEquals("Transfer trip to string", expected, trip3.getStops());
	}

	/**
	 * Test get time.
	 */
	@Test
	public void testGetTime() {
		long expected = 52200000l;
		assertEquals("Transfer trip to string", expected, trip1.getTime());
	}


	/**
	 * Test get listof flight.
	 */
	@Test
	public void testGetListofFlight() {
		List<Flight> expected = new ArrayList<Flight>();
		expected.add(f3);
		expected.add(f4);
		assertEquals("Transfer trip to string", expected, trip3.getListofFlight());
		
	}

	/**
	 * Test get set trip details.
	 */
	@Test
	public void testGetSetTripDetails() {
		String expected = "test";
		Trip test = new Trip();
		test.setTripDetails(expected);
		assertEquals("Transfer trip to string", expected, test.getTripDetails());
	}


	/**
	 * Test get set local departure time.
	 */
	@Test
	public void testGetSetLocalDepartureTime() {
		String expected = "test";
		Trip test = new Trip();
		test.setLocalDepartureTime(expected);
		assertEquals("Transfer trip to string", expected, test.getLocalDepartureTime());
	}

	/**
	 * Test get set local arrival time.
	 */
	@Test
	public void testGetSetLocalArrivalTime() {
		String expected = "test";
		Trip test = new Trip();
		test.setLocalArrivalTime(expected);
		assertEquals("Transfer trip to string", expected, test.getLocalArrivalTime());
	}


	/**
	 * Test get set cabin swap flag.
	 */
	@Test
	public void testGetSetCabinSwapFlag() {
		String expected = FBNConstants.NO;
		Trip test = new Trip();
		test.setCabinSwapFlag(expected);
		assertEquals("Transfer trip to string", expected, trip1.getCabinSwapFlag());
	}


}
