package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;
import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;

/**
 * These are unit tests for Class CachingSystem.
 * 
 * @author Team Blizzard.
 */
public class CachingSystemTest {

	/**
	 * Test get flights.
	 */
	@Test
	public void testGetFlights() {
		ReservationSystem.resetDB();	
		CachingSystem.setVerbose(false);
		int expected = CachingSystem.getFlights("BOS", 2015, 5,10).size(); // values from db (not cached yet)
		int actual = CachingSystem.getFlights("BOS", 2015, 5,10).size(); // cached values
		if(expected == 0 ) fail("no flights retreived"); // there should be some flights on that date
		else assertEquals("Get Flights", expected, actual);	
	}

	/**
	 * Test get aircrafts to cache.
	 */
	@Test
	public void testGetAircraftsToCacheAndGetAircraft() {
		CachingSystem.setVerbose(false);
		CachingSystem.getAircraftsToCache();
		int expected = 124;
		int actual = CachingSystem.getAircraft("A320").CoachSeats;
	    assertEquals("Available Aircraft Seats", expected, actual);		

	}

	/**
	 * Test get airports to cache.
	 */
	@Test
	public void testGetAirportsToCacheAndGetAirport() {
		CachingSystem.getAirportsToCache();
		String expected = "Logan International";
		String actual = CachingSystem.getAirport("BOS").name;
	    assertEquals("Get Airports", expected, actual);			
	}


	/**
	 * Test set is caching.
	 */
	@Test
	public void testSetIsCaching() {
		boolean expected = false;
		CachingSystem.setCaching(expected);
		boolean actual = CachingSystem.isCaching();
	    assertEquals("Caching False", expected, actual);			
		expected = true;
		CachingSystem.setCaching(expected);
		actual = CachingSystem.isCaching();
	    assertEquals("Caching True", expected, actual);		
  }
}
