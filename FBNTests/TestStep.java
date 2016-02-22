package cs509Blizzard.FBNTests;

/**
 * The Class TestStep is a helper class for the TestCase class. It is a data structure that holds information about each step. .
 * 
 * @author Ermal Toto
 */
public class TestStep {
	
	/** The action. */
	public String action;
	
	/** The expected result. */
	public String expectedResult;  
	
	/** The actual result. */
	public String actualResult;
	
	/** The pass. */
	public boolean pass;
	
	/**
	 * Instantiates a new test step.
	 *
	 * @param action the action
	 * @param expectedResult the expected result
	 * @param actualResult the actual result
	 */
	TestStep(String action, String expectedResult, String actualResult)
	{
		this.action = action;
		this.expectedResult = expectedResult;
		this.actualResult = actualResult;
		this.pass = expectedResult.equals(actualResult);
	}
}
