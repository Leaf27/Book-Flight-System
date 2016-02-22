package cs509Blizzard.FBNDataStructures;

 /**
  * The Class Aircraft contains the necessary information to define a aircraft. 
 *
 * @author Huan ye <hye@wpi.edu>
 * @version 1.0
 * @since 2015-3-15
 *
 */
public class Aircraft { 
 
  /** The Manufacturer, suggesting which company builds this aircraft.*/
  public String Manufacturer;
  
  /** The aircraft model, indicating which type this aircraft is.*/
  public String Model;
  
  /** The number of first class seats */
  public int FirstClassSeats;
  
  /** The number of coach seats. */
  public int CoachSeats;
  
  /**
   * Instantiates a new aircraft.
   *
   * @param Manufacturer      the manufacturer of this flight
   * @param Model             the model of this flight
   * @param FirstClassSeats   the number of first class seats 
   * @param CoachSeats        the number of coach seats
   */
public Aircraft(String Manufacturer, String Model,int FirstClassSeats,int CoachSeats){
	  this.Manufacturer= Manufacturer;
	  this.Model=Model;
	  this.FirstClassSeats=FirstClassSeats;
	  this.CoachSeats= CoachSeats;
  }
  
}


