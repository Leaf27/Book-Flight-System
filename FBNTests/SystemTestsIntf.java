
package cs509Blizzard.FBNTests;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JButton;

import cs509Blizzard.FlyByNightAPI;
import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;

/**
 * The Class SystemTestsIntf is a graphical interface to run system tests. It partly
 * uses SystemTests and it adds additional functionality. It also provides
 * an easy way to reset and unlock the database. This is  not  part of the official
 * flybynight package, and it is included for testing purposes only. All contents in this
 * class are subject to continuous change from all members of team Blizzard . 
 * 
 * @author    Ermal Toto <toto@wpi.edu>
 * @see       SystemTests
 * 
 */
 
public class SystemTestsIntf {

	/** The frame. */
	private final JFrame frame = new JFrame();
	
	/** The system tester. */
	private final SystemTests systemTester = new SystemTests();
	private final FlyByNightAPI FLBApiTester = new FlyByNightAPI();


	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SystemTestsIntf window = new SystemTestsIntf();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SystemTestsIntf() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JButton resetDatabase = new JButton("Reset Database");
		resetDatabase.setBounds(107, 17, 210, 29);
		frame.getContentPane().add(resetDatabase);

		final JButton runTest1 = new JButton("Run Reserve Test");
		runTest1.setBounds(107, 109, 210, 29);
		frame.getContentPane().add(runTest1);

		final JButton unlockDb = new JButton("Unlock Database");
		unlockDb.setBounds(107, 58, 210, 29);
		frame.getContentPane().add(unlockDb);

		resetDatabase.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				FLBApiTester.resetDB();
			}
		});  

		unlockDb.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				FLBApiTester.unlockDatabase();
			}
		});  

		runTest1.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				FLBApiTester.resetDB();
				FLBApiTester.setVerboseSearch(false);
				Calendar dayOne = Calendar.getInstance();
				dayOne.set(2015,5 - 1, 12); 
				systemTester.bookTrip(systemTester.searchTrips("Before Purchase: ","reset database","SFO", "EWR", dayOne,1,60,180,53,80)); 
				systemTester.searchTrips("After Purchase: ","Book the first trip that is found","SFO", "EWR", dayOne,1,60,180,52,79);
		     
				//systemTester.bookTrip("BOS", "SFO", dayOne);   	       
			}
		});  		

	}
}
