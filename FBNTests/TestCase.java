package cs509Blizzard.FBNTests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * The Class TestCase facilitates auto documentation of the automated integrated test.
 * This class models general test cases, and it is not limited to integration tests. 
 * It uses the TestStep (@see TestStep) class to document each step. 
 * 
 * @author Ermal Toto
 */
public class TestCase {
	
	/** The project name. */
	public String projectName = "FlyByNight";
	
	/** The test case description. */
	public String testCaseDescription;
	
	/** The test module. */
	public String testModule;
	
	/** The test requirement. */
	public String testRequirement;
	
	/** The author. */
	public String author = "TeamBlizzard"; 
	
	/** The test goal. */
	public String testGoal;
	
	/** The test setup. */
	public String testSetup;
	
	/** The pre conditions. */
	public String preConditions = "none";
	
	/** The date format. */
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/** The date. */
	private Date date = new Date();
	
	/** The test date. */
	public String testDate = dateFormat.format(this.date);
	
	/** The test build. */
	public long testBuild = -1;
	
	/** The test run. */
	public long testRun = -1;
	
	/** The test execution retry. */
	public long testExecutionRetry = -1;
	
	/** The user case. */
	public String userCase;
	
	/** The step count. */
	private int stepCount = 1;
	
	/** The pass flag. */
	private boolean passFlag = true;
	
	/** The list of steps. */
	private List<TestStep> listOfSteps = new ArrayList<TestStep>();
 	
 	/**
	  * Adds the step for the test. Each step utilizes the Test Step class which does basic asserts and documentation.
	  *
	  * @param action the action
	  * @param expectedResult the expected result
	  * @param actualResult the actual result
	  */
	 public void addStep(String action, String expectedResult,  String actualResult)
	{
		listOfSteps.add(new TestStep(action,expectedResult,actualResult));
		if(!expectedResult.equals(actualResult)) this.passFlag = false;;
	}
 	
 	/**
	  * Prints the test case.
	  */
	 public void printTestCase()
 	{
 		System.out.println("         Project Name: " + this.projectName);
 		System.out.println("Test Case Description: " + this.testCaseDescription);
 		System.out.println("          Test Module: " + this.testModule);
 		System.out.println("    Test Requirements: " + this.testRequirement);
 		System.out.println("           Written By: " + this.author);
 		System.out.println("                Goals: " + this.testGoal);
 		System.out.println("           Test Setup: " + this.testSetup);
 		System.out.println("       Pre-Conditions: " + this.preConditions);
 		System.out.println("                 Date: " + this.testDate);
 		System.out.println("            User Case: " + this.userCase);
 		TestStep step = null;
		System.out.format("%5s%30s%30s%30s%30s",
			    "Step", "Action", " Expected Result", "Pass", "Actual Result");
		System.out.println("");
 		for(int i = 0; i < listOfSteps.size(); i++)
 		{
 			step = listOfSteps.get(i);
 			System.out.format("%5s%30s%30s%30s%30s", i, step.action, step.expectedResult, step.pass, step.actualResult);
 			System.out.println("");
 		}

 		System.out.println("\n\n OVERALL ASSESSMENT: " + this.passFlag);
	
 	}
}
