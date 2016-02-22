
package cs509Blizzard.FBNTests;

import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cs509Blizzard.FlyByNightAPI;
import cs509Blizzard.FBNDataStructures.Flight;
import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.ReservationSystemInteraction.CachingSystem;




/**
 * The Class SystemTests  is used to run system tests by all team members. And it is subject to 
 * continuous change. It is not a part of the final FlyByNight product. 
 * 
 * @author    Ermal Toto <toto@wpi.edu>
 * @author    Huan Ye
 * @author 	  Shirui Zhang
 * @author    Yiheng Liu
 * 
 */
public class SystemTests {
	
	/** The FLB api tester. */
	private  static FlyByNightAPI FLBApiTester = new FlyByNightAPI();

	/**
	 * This test searches for flights from origin to destination, books the first trip
	 * then searches again. The console logs then demonstrate a full data loop. 
	 *
	 * @param origin the origin
	 * @param destination the destination
	 * @param departureDate the departure date
	 */
	public static Trip searchTrips(String description, String precondition, String origin, String destination, Calendar departureDate, int numStops, int minLayover, int maxLayover, int f1seats, int f2seats)
	{
		TestCase testCaseSearchTrips = new TestCase();
		testCaseSearchTrips.testCaseDescription = description + "This test case maps to the searchTrip usecase and tests functionality that related to searching for trips.";
		testCaseSearchTrips.preConditions = precondition;	
		FLBApiTester.setOrigin(origin);
		testCaseSearchTrips.addStep("set origin airport", origin,  FLBApiTester.getOrigin());

		FLBApiTester.setDestination(destination);
		testCaseSearchTrips.addStep("set destination airport",  destination,   FLBApiTester.getDestination());

		FLBApiTester.setRoundTrip(false);
		testCaseSearchTrips.addStep("set round trip", "(Bool)" + false,  "(Bool)" + FLBApiTester.isRoundTrip());

		FLBApiTester.setOvernight(false);	
		testCaseSearchTrips.addStep("set overnight", "(Bool)" +  false,  "(Bool)" + FLBApiTester.isOvernight());

		FLBApiTester.setCabinClass(FBNConstants.COACH);		
		testCaseSearchTrips.addStep("set cabin", FBNConstants.COACH, FLBApiTester.getCabinClass());
		

		FLBApiTester.setMaxNumberOfStops(numStops);
		testCaseSearchTrips.addStep("set stops", "(int)" + numStops, "(int)" + FLBApiTester.getMaxNumberOfStops());

		FLBApiTester.setMinLayover(minLayover); // In minutes 
		testCaseSearchTrips.addStep("set min layover", "(int)" + minLayover,  "(int)" + FLBApiTester.getMinLayover());

		FLBApiTester.setMaxLayover(maxLayover); // In minutes
		testCaseSearchTrips.addStep("set max layover", "(int)" + maxLayover,  "(int)" + FLBApiTester.getMaxLayover());

		FLBApiTester.setDepartureDate(departureDate);
		String engineDate = "" + FLBApiTester.getEngine1().getYear();
		engineDate += "-" + FLBApiTester.getEngine1().getMonth();
		engineDate += "-" + FLBApiTester.getEngine1().getDay();
		testCaseSearchTrips.addStep("set departureDate", "2015-5-12",  engineDate);

		FLBApiTester.searchDepartingTrips();
		TripList departingTripList = FLBApiTester.getDepartingTrips();  
		int numberOfTrips = departingTripList.getTrips().size();
		testCaseSearchTrips.addStep("search trips (count 3)","(int)" + 3, "(int)" + numberOfTrips);



		Trip firstTrip = null;
		if(departingTripList != null)
			 firstTrip = departingTripList.getTrip(0);

		int flight1 = CachingSystem.getAvailableSeats(firstTrip.getListofFlight().get(0),FBNConstants.COACH);
		int flight2 = CachingSystem.getAvailableSeats(firstTrip.getListofFlight().get(1),FBNConstants.COACH);
		testCaseSearchTrips.addStep("check seats on flight 1","(int)" + f1seats, "(int)" + flight1);
		testCaseSearchTrips.addStep("check seats on flight 2","(int)" + f2seats, "(int)" + flight2);


		testCaseSearchTrips.printTestCase();
		return firstTrip;
	}

	/**
	 * This test searches for flights from origin to destination, books the first trip
	 * then searches again. The console logs then demonstrate a full data loop. 
	 *
	 * @param origin the origin
	 * @param destination the destination
	 * @param departureDate the departure date
	 */
	public static void bookTrip(Trip tripToBook)
	{
		TestCase testCaseBookTrip = new TestCase();
		testCaseBookTrip.testCaseDescription = "This test case maps to the purchase ticket usecase and tests functionality that related to booking a one way trip.";

		FLBApiTester.ReserveTrip(tripToBook,null, FBNConstants.COACH);
		FLBApiTester.searchDepartingTrips();
		testCaseBookTrip.printTestCase();
	}	
	
	/**
	 * The main method. Put test initializations here. 
	 *
	 * @param args the arguments
	 */
	public static void main(String [ ] args)
	{
    
		FLBApiTester.resetDB();
		FLBApiTester.setVerboseSearch(false);
		Calendar dayOne = Calendar.getInstance();
		dayOne.set(2015,5 - 1, 12); 
		bookTrip(searchTrips("Before Purchase: ","reset database","SFO", "EWR", dayOne,1,60,180,53,80)); 
		searchTrips("After Purchase: ","Book the first trip that is found","SFO", "EWR", dayOne,1,60,180,52,79);
	}
}
