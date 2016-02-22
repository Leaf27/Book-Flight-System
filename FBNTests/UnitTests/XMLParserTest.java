package cs509Blizzard.FBNTests.UnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import cs509Blizzard.FBNDataStructures.Aircraft;
import cs509Blizzard.FBNDataStructures.Airport;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;
import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;
import cs509Blizzard.ReservationSystemInteraction.XMLParser;


/**
 * These are unit tests for the  Class XMLParserTest.
 * 
 * @author TeamBlizzard
 */
public class XMLParserTest {

	/**
	 * Test parse departure.
	 */
	@Test
	public void testParseDeparture(){
		
	  try{
		String info= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Flights><Flight Airplane=\"A340\" FlightTime=\"137\" Number=\"1781\"><Departure><Code>BOS</Code><Time>2015 May 10 00:07 GMT</Time></Departure><Arrival><Code>MEM</Code><Time>2015 May 10 02:24 GMT</Time></Arrival><Seating><FirstClass Price=\"$323.25\">28</FirstClass><Coach Price=\"$38.60\">23</Coach></Seating></Flight></Flights>";
		List<Flight> flights = new ArrayList<>();
		CachingSystem caching=new CachingSystem();
		caching.getAirportsToCache();
		flights=XMLParser.parseDeparture(info);		
		
		assertEquals("A340",flights.get(0).airplaneModel);
		assertEquals(137*60*1000,flights.get(0).flightTime,0.0);
		assertEquals("1781",flights.get(0).flightNumber);
		assertEquals(1431216420000L,flights.get(0).departureTime);
		assertEquals("MEM",flights.get(0).arrivalCode);
		assertEquals(1431224640000L,flights.get(0).arrivalTime);
		assertEquals(323.25,flights.get(0).firstClassPrice,0.0);
		assertEquals(28,flights.get(0).firstClassSeats);
		assertEquals(38.60,flights.get(0).coachClassPrice,0.0);
		assertEquals(23,flights.get(0).coachClassSeats);
	  } 
	  catch(Exception e){
		  fail("parseDeparture goes wrong! ");
	  }
	}


	/**
	 * Test get air craft.
	 */
	@Test
	public void testGetAirCraft() {
		try{
		HashMap<String, Aircraft> aircraft= new HashMap<String, Aircraft>();
		aircraft=XMLParser.getAirCraft("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Airplanes><Airplane Manufacturer=\"Airbus\" Model=\"A310\"><FirstClassSeats>24</FirstClassSeats><CoachSeats>200</CoachSeats></Airplane></Airplanes>");
		assertEquals("Airbus",aircraft.get("A310").Manufacturer);
		assertEquals("A310",aircraft.get("A310").Model);
		assertEquals(24,aircraft.get("A310").FirstClassSeats);
		assertEquals(200,aircraft.get("A310").CoachSeats);		
		}
		catch(Exception e){
			fail("get aircraft goes wrong!");
		}
		}
	
	/**
	 * Test get airport.
	 */
	@Test
	public void testGetAirport() {
		try{
			HashMap<String, Airport> airport= new HashMap<String, Airport>();
			airport=XMLParser.getAirport("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Airports><Airport Code=\"ATL\" Name=\"Hartsfield-Jackson Atlanta International\"><Latitude>33.641045</Latitude><Longitude>-84.427764</Longitude></Airport></Airports>");
			assertEquals("ATL",airport.get("ATL").code);
			assertEquals("Hartsfield-Jackson Atlanta International",airport.get("ATL").name);
			assertEquals(33.641045,airport.get("ATL").latitude,0.0);
			assertEquals(-84.427764,airport.get("ATL").longtitude,0.0);		
			}
			catch(Exception e){
				fail("get airport goes wrong!");
			}
	}

}
