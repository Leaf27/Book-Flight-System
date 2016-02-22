package cs509Blizzard.FBNUtilities;

import javax.swing.JOptionPane;

import cs509Blizzard.ReservationSystemInteraction.ReservationSystem;

// TODO: Auto-generated Javadoc
/**
 * The Class FlyByNightPop generates pop-up messages for users.
 * 
 *  @author    Yiheng Liu <yliu12@wpi.edu>
 *  @version   1.0
 *  @since     2015-02-01 
 *  @see 	   ReservationSystem
 */

public class FlyByNightPop {
	
	/**
	 * Info box.
	 *
	 * @param infoMessage the info message
	 * @param titleBar the title bar
	 */
	public static void infoBox(String infoMessage, String titleBar){
		 JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Error box.
	 *
	 * @param infoMessage the info message
	 * @param titleBar the title bar
	 */
	public static void errorBox(String infoMessage, String titleBar){
		 JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
	}
}
