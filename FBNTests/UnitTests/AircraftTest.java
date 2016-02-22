package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs509Blizzard.FBNDataStructures.Aircraft;

/**
 * Unit Tests for Class Aircraft
 * 
 * @author Team Blizzard.
 */
public class AircraftTest {

	/**
	 * Test aircraft.
	 */
	@Test
	public void testAircraft() {
		Aircraft airplane=new Aircraft("Boing","A370",20,30);
		String Manufacturer = "Boing";
		String Model = "A370";
		int FirstClassSeats = 20;
		int CoachSeats = 30;
		assertEquals(Manufacturer,airplane.Manufacturer);
		assertEquals(Model,airplane.Model);
		assertEquals(FirstClassSeats,airplane.FirstClassSeats);
		assertEquals(CoachSeats,airplane.CoachSeats);		
	}

}
