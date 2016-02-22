package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs509Blizzard.FBNDataStructures.Airport;
/**
 * Unit Tests for airport class
 * 
 * * @author Team Blizzard.
 */

public class AirportTest {


	/**
	 * Test airport.
	 */
	@Test
	public void testAirport() {
		Airport aAirport = new Airport("BOS","BOSTON AIRPORT", 130.005,20.896, "GMT");
		String code = "BOS";
		String name ="BOSTON AIRPORT";
		double latitude = 130.005;
		double longtitude = 20.896;
		String timeZoneData= "GMT";
		
		assertEquals(code, aAirport.code);
		assertEquals(name, aAirport.name);
		assertEquals(latitude, aAirport.latitude,0.0);
		assertEquals(longtitude, aAirport.longtitude,0.0);
		assertEquals(timeZoneData, aAirport.timeZoneData);
		
		
	}
}

