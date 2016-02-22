package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;
import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;
import cs509Blizzard.SearchEngine.SearchEngine;

/**
 * Unit Tests for SearchEngine Class.
 * 
 * @author TeamBlizzard
 */
public class SearchEngineTest {

	/** The engine. */
	SearchEngine engine = new SearchEngine();

	/**
	 * Test set get origin.
	 */
	@Test
	public void testSetGetOrigin() {
		String expected = "BOS";
		engine.setOrigin(expected);
		String actual = engine.getOrigin();
	    assertEquals("Set and Get Origin", expected, actual);
	}


	/**
	 * Test set get cabinclass.
	 */
	@Test
	public void testSetGetCabinclass() {
		String expected = FBNConstants.COACH;
		engine.setCabinclass(expected);
		String actual = engine.getCabinclass();
	    assertEquals("Set and Get Cabin Class", expected, actual);
	}

	/**
	 * Test set get destination.
	 */
	@Test
	public void testSetGetDestination() {
		String expected = "SFO";
		engine.setDestination(expected);
		String actual = engine.getDestination();
	    assertEquals("Set and Get Destination", expected, actual);	
	}

	/**
	 * Test set get min connecting time.
	 */
	@Test
	public void testSetGetMinConnectingTime() {
		long expected = 30;
		engine.setMinConnectingTime(expected);
		long actual = engine.getMinConnectingTime()  / FBNConstants.MSINMINUTE;
	    assertEquals("Set and Get Min Layover", expected, actual);		
	}

	/**
	 * Test set get max connecting time.
	 */
	@Test
	public void testSetGetMaxConnectingTime() {
		long expected = 180;
		engine.setMaxConnectingTime(expected);
		long actual = engine.getMaxConnectingTime()  / FBNConstants.MSINMINUTE;
	    assertEquals("Set and Get Max Layover", expected, actual);		
	}

	/**
	 * Test set is include overnight flights.
	 */
	@Test
	public void testSetIsIncludeOvernightFlights() {
		boolean expected = true;
		engine.setIncludeOvernightFlights(expected);
		boolean actual = engine.isIncludeOvernightFlights();
	    assertEquals("Set and Is Overnight Flight Check", expected, actual);		
	}

	/**
	 * Test set get year.
	 */
	@Test
	public void testSetGetYear() {
		int expected = 2015;
		engine.setYear(expected);;
		int actual = engine.getYear();
	    assertEquals("Set and Get Year", expected, actual);		
	}


	/**
	 * Test set get month.
	 */
	@Test
	public void testSetGetMonth() {
		int expected = 5;
		engine.setMonth(expected);;
		int actual = engine.getMonth();
	    assertEquals("Set and Get Month", expected, actual);		
	}



	/**
	 * Test set get day.
	 */
	@Test
	public void testSetGetDay() {
		int expected = 10;
		engine.setDay(expected);;
		int actual = engine.getDay();
	    assertEquals("Set and Get Day", expected, actual);	
	}

	/**
	 * Test set is verbose search.
	 */
	@Test
	public void testSetIsVerboseSearch() {
		boolean expected = true;
		engine.setVerboseSearch(expected);
		boolean actual = engine.isVerboseSearch();
	    assertEquals("Set and Is Verbose Debugging", expected, actual);			
	}
	
	/**
	 * Test search.
	 */
	@Test
	public void testSearch() {
		ReservationSystem.resetDB();
		engine.setOrigin("BOS");
		engine.setDestination("SFO");
		engine.setCabinclass(FBNConstants.COACH);
		engine.setDay(10);
		engine.setMonth(5);;
		engine.setYear(2015);;
		engine.setMinConnectingTime(30);
		engine.setMaxConnectingTime(180);
		engine.setIncludeOvernightFlights(true);
		engine.setMAXNUMSTOPS(2);
		engine.setVerboseSearch(false);
		TripList testTrips = engine.search();
		int expected = 23;
		int actual = testTrips.getTrips().size();
	    assertEquals("On DB reset 10 Trips Expected", expected, actual);	
	    
	    
		ReservationSystem.resetDB();
		engine.setOrigin("BOS");
		engine.setDestination("SFO");
		engine.setCabinclass(FBNConstants.COACH);
		engine.setDay(20);
		engine.setMonth(5);;
		engine.setYear(2015);;
		engine.setMinConnectingTime(30);
		engine.setMaxConnectingTime(180);
		engine.setIncludeOvernightFlights(true);
		engine.setMAXNUMSTOPS(2);
		engine.setVerboseSearch(false);
		testTrips = engine.search();
		expected = 0;
		actual = testTrips.getTrips().size();
	    assertEquals("On DB reset 10 Trips Expected", expected, actual);		    
	}

}
