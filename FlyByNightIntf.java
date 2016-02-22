package cs509Blizzard;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import cs509Blizzard.FBNDataStructures.Trip;
import cs509Blizzard.FBNDataStructures.TripList;
import cs509Blizzard.FBNUtilities.Autocomplete;
import cs509Blizzard.FBNUtilities.FBNConstants;
import cs509Blizzard.FBNUtilities.FlyByNightPop;
import cs509Blizzard.RangeSlider.RangeSlider;
import cs509Blizzard.FBNUtilities.ComparableTime;


/**
 * The Class FlyByNightIntf is the main graphical interface for users. Users can search,
 * sort, filter and book tickets through this interface. It is constructed by WindowBuilder
 *  and interacts with Class FlyByNightAPI only.
 * 
 * @author   Yiheng Liu <yliu12@wpi.edu>
 * @author   Ermal Toto <toto@wpi.edu>
 * @author   Shirui Zhang <szhang3@wpi.edu>
 * @author   Huan Ye <hye@wpi.edu>
 * @version  1.0
 * @since    2015-02-01 
 * 
 */
public class FlyByNightIntf {

	/** The frame. */
	private final JFrame frame = new JFrame();

	/** The flybynight. */
	private FlyByNightAPI flybynight = new FlyByNightAPI();

	/** The departing flights table. */
	private JTable departTable;

	/** The returning flights table. */
	private JTable returnTable;

	/** The message box. */
	private final FlyByNightPop messageBox = new FlyByNightPop();


	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlyByNightIntf window = new FlyByNightIntf();
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
	public FlyByNightIntf() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setResizable(false);
		frame.setBounds(100, 100, 1006, 630);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollReturn = new JScrollPane();
		scrollReturn.setBounds(499, 289, 456, 230);
		scrollReturn.setName("");
		frame.getContentPane().add(scrollReturn);

		returnTable = new JTable();
		returnTable.setAutoCreateRowSorter(true);
		returnTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Departure Time", "Arrival Time", "Stops", "Flight Duration","Total Cost","Mixed Cabin"
				}
				) {
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Integer.class, ComparableTime.class, Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		returnTable.getColumnModel().getColumn(0).setPreferredWidth(98);
		returnTable.getColumnModel().getColumn(1).setPreferredWidth(103);
		returnTable.setName("returnTable");
		scrollReturn.setViewportView(returnTable);

		JScrollPane scrollDepart = new JScrollPane();
		scrollDepart.setName("");
		scrollDepart.setBounds(59, 289, 435, 230);
		frame.getContentPane().add(scrollDepart);

		departTable = new JTable();
		departTable.setAutoCreateRowSorter(true);
		departTable.setName("departTable");
		departTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Departure Time", "Arrival Time", "Stops", "Flight Duration", "Total Cost", "Mixed Cabin"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, Integer.class, ComparableTime.class, Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		departTable.getColumnModel().getColumn(0).setPreferredWidth(103);
		departTable.getColumnModel().getColumn(1).setPreferredWidth(99);
		scrollDepart.setViewportView(departTable);

		final JLabel lblNewLabel = new JLabel("Origin");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel.setBounds(94, 126, 52, 23);
		frame.getContentPane().add(lblNewLabel);

		final JLabel lblNewLabel_1 = new JLabel("Destination");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel_1.setBounds(378, 127, 85, 20);
		frame.getContentPane().add(lblNewLabel_1);



		final JTextField originText = new JTextField();	
		ArrayList<String> keywords = flybynight.generateAutoCompleteAiportList();
		Autocomplete autoCompleteOrigin = new Autocomplete(originText, keywords);
		originText.getDocument().addDocumentListener(autoCompleteOrigin);

		// Maps the tab key to the commit action, which finishes the autocomplete
		// when given a suggestion
		originText.getInputMap().put(KeyStroke.getKeyStroke("TAB"), FBNConstants.COMMIT_ACTION);
		originText.getActionMap().put(FBNConstants.COMMIT_ACTION, autoCompleteOrigin.new CommitAction());

		originText.setBounds(147, 123, 219, 28);
		frame.getContentPane().add(originText);
		originText.setColumns(10);

		final JDateChooser departureDate = new JDateChooser();
		departureDate.setBounds(147, 160, 134, 28);
		frame.getContentPane().add(departureDate);

		final JDateChooser returnDate = new JDateChooser();
		returnDate.setBounds(475, 160, 134, 28);
		frame.getContentPane().add(returnDate);

		/*
		final JLabel lblNewLabel_2 = new JLabel("Welcome to FlyByNight!");
		lblNewLabel_2.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
		lblNewLabel_2.setBounds(370, 16, 200, 28);
		frame.getContentPane().add(lblNewLabel_2);
		 */
		final JTextField destinationText = new JTextField();

		Autocomplete autoCompleteDestination = new Autocomplete(destinationText, keywords);
		destinationText.getDocument().addDocumentListener(autoCompleteDestination);

		// Maps the tab key to the commit action, which finishes the autocomplete
		// when given a suggestion
		destinationText.getInputMap().put(KeyStroke.getKeyStroke("TAB"), FBNConstants.COMMIT_ACTION);
		destinationText.getActionMap().put(FBNConstants.COMMIT_ACTION, autoCompleteDestination.new CommitAction());

		//destinationText.setColumns(10);
		destinationText.setBounds(475, 123, 219, 28);
		frame.getContentPane().add(destinationText);

		final JCheckBox roundTripCheck = new JCheckBox("Round trip");
		roundTripCheck.setFont(new Font("Arial", Font.PLAIN, 13));
		roundTripCheck.setBounds(706, 126, 91, 23);
		frame.getContentPane().add(roundTripCheck);

		final JCheckBox overnightCheck = new JCheckBox("Overnight");
		overnightCheck.setFont(new Font("Arial", Font.PLAIN, 13));
		overnightCheck.setBounds(841, 126, 91, 23);
		frame.getContentPane().add(overnightCheck);
		overnightCheck.setSelected(true);

		final JLabel lblDeparture = new JLabel("Depart");
		lblDeparture.setFont(new Font("Arial", Font.BOLD, 15));
		lblDeparture.setBounds(94, 165, 52, 23);
		frame.getContentPane().add(lblDeparture);

		final JLabel lblReturn = new JLabel("Return");
		lblReturn.setFont(new Font("Arial", Font.BOLD, 15));
		lblReturn.setBounds(411, 166, 52, 20);
		frame.getContentPane().add(lblReturn);

		final JLabel lblClass = new JLabel("Class");
		lblClass.setFont(new Font("Arial", Font.PLAIN, 13));
		lblClass.setBounds(657, 168, 33, 20);
		frame.getContentPane().add(lblClass);

		final JComboBox flightClass = new JComboBox();
		flightClass.setBounds(690, 164, 112, 27);
		frame.getContentPane().add(flightClass);
		flightClass.addItem(FBNConstants.COACH);
		flightClass.addItem(FBNConstants.FIRST);	

		final SpinnerNumberModel minLayoverModel = new SpinnerNumberModel(FBNConstants.MINLAYOVER, FBNConstants.LAYOVER_LOW, FBNConstants.LAYOVER_HIGH, 1);   //default value,lower bound,upper bound,increment by
		final JSpinner minLayover = new JSpinner(minLayoverModel);
		minLayover.setBounds(199, 200, 73, 28);
		frame.getContentPane().add(minLayover);
		minLayover.setValue(new Integer(FBNConstants.MINLAYOVER));


		final JLabel minLayoverLabel = new JLabel("Min Layover");
		minLayoverLabel.setFont(new Font("Arial", Font.BOLD, 15));
		minLayoverLabel.setBounds(94, 203, 112, 23);
		frame.getContentPane().add(minLayoverLabel);

		final JLabel maximumLayoverLabel = new JLabel("Max Layover");
		maximumLayoverLabel.setFont(new Font("Arial", Font.BOLD, 15));
		maximumLayoverLabel.setBounds(410, 203, 91, 23);
		frame.getContentPane().add(maximumLayoverLabel);

		final SpinnerNumberModel maxLayoverModel = new SpinnerNumberModel(FBNConstants.MAXLAYOVER, FBNConstants.LAYOVER_LOW, FBNConstants.LAYOVER_HIGH, 1);   //default value,lower bound,upper bound,increment by
		final JSpinner maxLayover = new JSpinner(maxLayoverModel);
		maxLayover.setBounds(513, 200, 73, 28);
		frame.getContentPane().add(maxLayover);
		maxLayover.setValue(new Integer(FBNConstants.MAXLAYOVER));

		final JLabel lblMin1 = new JLabel("min");
		lblMin1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblMin1.setBounds(282, 205, 21, 20);
		frame.getContentPane().add(lblMin1);

		final JLabel lblMin2 = new JLabel("min");
		lblMin2.setFont(new Font("Arial", Font.PLAIN, 13));
		lblMin2.setBounds(588, 205, 21, 20);
		frame.getContentPane().add(lblMin2);		

		final JButton searchFlights = new JButton("Search");
		searchFlights.setFont(new Font("Arial", Font.BOLD, 13));
		searchFlights.setBounds(434, 248, 117, 29);
		frame.getContentPane().add(searchFlights);

		final JButton researveFlights = new JButton("Reserve");
		researveFlights.setFont(new Font("Arial", Font.BOLD, 13));
		researveFlights.setBounds(434, 558, 117, 29);
		frame.getContentPane().add(researveFlights);

		final JButton detailFlights = new JButton("Flight Details");
		detailFlights.setFont(new Font("Arial", Font.BOLD, 13));
		detailFlights.setBounds(434, 531, 117, 29);
		frame.getContentPane().add(detailFlights);

		final SpinnerNumberModel numStopsModel = new SpinnerNumberModel(FBNConstants.MIN_NUM_STOPS, FBNConstants.MIN_NUM_STOPS, FBNConstants.MAX_NUM_STOPS, 1);   //default value,lower bound,upper bound,increment by
		final JSpinner numberOfStops = new JSpinner(numStopsModel);
		numberOfStops.setBounds(859, 162, 73, 28);
		frame.getContentPane().add(numberOfStops);

		final JLabel lblStops = new JLabel("Stops");
		lblStops.setFont(new Font("Arial", Font.PLAIN, 13));
		lblStops.setBounds(823, 167, 44, 20);
		frame.getContentPane().add(lblStops);


		final JLabel outboundDepartureRangeValue1 = new JLabel();
		final JLabel outboundDepartureRangeValue2 = new JLabel();

		outboundDepartureRangeValue1.setBounds(198, 240, 63, 16);
		outboundDepartureRangeValue2.setBounds(345, 240, 48, 16);

		final RangeSlider outboundDepartureRange = new RangeSlider();
		outboundDepartureRange.setBounds(199, 261, 194, 16);

		outboundDepartureRange.setMinimum(0);
		outboundDepartureRange.setMaximum(24);
		outboundDepartureRange.setValue(0);

		frame.getContentPane().add(outboundDepartureRange);
		frame.getContentPane().add(outboundDepartureRangeValue1);
		frame.getContentPane().add(outboundDepartureRangeValue2);

		outboundDepartureRangeValue1.setText("From:" + String.valueOf(outboundDepartureRange.getValue()) + "h");
		outboundDepartureRangeValue2.setText("To:" + String.valueOf(outboundDepartureRange.getUpperValue()) + "h");

		// Add listener to update display.
		outboundDepartureRange.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				outboundDepartureRangeValue1.setText("From:" + String.valueOf(slider.getValue()) + "h");
				outboundDepartureRangeValue2.setText("To:" + String.valueOf(slider.getUpperValue()) + "h");
				flybynight.setDepartureFiltersOutbound(slider.getValue(), slider.getUpperValue());      
				updateTablesWithFilteredResults();

			}
		});		




		final JLabel inboundDepartureRangeValue1 = new JLabel();
		final JLabel inboundDepartureRangeValue2 = new JLabel();

		inboundDepartureRangeValue1.setBounds(723, 248, 63, 16);
		inboundDepartureRangeValue2.setBounds(869, 252, 48, 16);

		final RangeSlider inboundDepartureRange = new RangeSlider();
		inboundDepartureRange.setBounds(723, 265, 194, 16);

		inboundDepartureRange.setMinimum(0);
		inboundDepartureRange.setMaximum(24);
		inboundDepartureRange.setValue(0);

		frame.getContentPane().add(inboundDepartureRange);
		frame.getContentPane().add(inboundDepartureRangeValue1);
		frame.getContentPane().add(inboundDepartureRangeValue2);

		inboundDepartureRangeValue1.setText("From:" + String.valueOf(inboundDepartureRange.getValue()) + "h");
		inboundDepartureRangeValue2.setText("To:" + String.valueOf(inboundDepartureRange.getUpperValue()) + "h");

		// Add listener to update display.
		inboundDepartureRange.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				inboundDepartureRangeValue1.setText("From:" + String.valueOf(slider.getValue()) + "h");
				inboundDepartureRangeValue2.setText("To:" + String.valueOf(slider.getUpperValue()) + "h");
				flybynight.setDepartureFiltersInbound(slider.getValue(), slider.getUpperValue());   
				updateTablesWithFilteredResults();
			}
		});		

		final JLabel inboundArrivalRangeValue1 = new JLabel();
		final JLabel inboundArrivalRangeValue2 = new JLabel();

		inboundArrivalRangeValue1.setBounds(723, 547, 63, 16);
		inboundArrivalRangeValue2.setBounds(869, 544, 48, 16);

		final RangeSlider inboundArrivalRange = new RangeSlider();
		inboundArrivalRange.setBounds(723, 531, 194, 16);

		inboundArrivalRange.setMinimum(0);
		inboundArrivalRange.setMaximum(24);
		inboundArrivalRange.setValue(0);

		frame.getContentPane().add(inboundArrivalRange);
		frame.getContentPane().add(inboundArrivalRangeValue1);
		frame.getContentPane().add(inboundArrivalRangeValue2);

		inboundArrivalRangeValue1.setText("From:" + String.valueOf(inboundArrivalRange.getValue()) + "h");
		inboundArrivalRangeValue2.setText("To:" + String.valueOf(inboundArrivalRange.getUpperValue()) + "h");

		// Add listener to update display.
		inboundArrivalRange.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				inboundArrivalRangeValue1.setText("From:" + String.valueOf(slider.getValue()) + "h");
				inboundArrivalRangeValue2.setText("To:" + String.valueOf(slider.getUpperValue()) + "h");
				flybynight.setArrivalFiltersInbound(slider.getValue(), slider.getUpperValue()); 
				updateTablesWithFilteredResults();
			}
		});			


		final JLabel outboundArrivalRangeValue1 = new JLabel();
		final JLabel outboundArrivalRangeValue2 = new JLabel();

		outboundArrivalRangeValue1.setBounds(199, 547, 63, 16);
		outboundArrivalRangeValue2.setBounds(345, 544, 48, 16);

		final RangeSlider outboundArrivalRange = new RangeSlider();
		outboundArrivalRange.setBounds(199, 531, 194, 16);

		outboundArrivalRange.setMinimum(0);
		outboundArrivalRange.setMaximum(24);
		outboundArrivalRange.setValue(0);

		frame.getContentPane().add(outboundArrivalRange);	
		frame.getContentPane().add(outboundArrivalRangeValue1);
		frame.getContentPane().add(outboundArrivalRangeValue2);

		outboundArrivalRangeValue1.setText("From:" + String.valueOf(outboundArrivalRange.getValue()) + "h");
		outboundArrivalRangeValue2.setText("To:" + String.valueOf(outboundArrivalRange.getUpperValue()) + "h");

		// Add listener to update display.
		outboundArrivalRange.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				outboundArrivalRangeValue1.setText("From:" + String.valueOf(slider.getValue()) + "h");
				outboundArrivalRangeValue2.setText("To:" + String.valueOf(slider.getUpperValue()) + "h");
				flybynight.setArrivalFiltersOutbound(slider.getValue(), slider.getUpperValue());  
				updateTablesWithFilteredResults();
			}
		});			



		final JLabel lblDepartureWindow = new JLabel("Departure Window:");
		lblDepartureWindow.setBounds(59, 261, 145, 16);
		frame.getContentPane().add(lblDepartureWindow);

		final JLabel label = new JLabel("Departure Window:");
		label.setBounds(588, 261, 145, 16);
		frame.getContentPane().add(label);		

		final JLabel lblArrivalWindow = new JLabel("Arrival Window:");
		lblArrivalWindow.setBounds(69, 531, 145, 16);
		frame.getContentPane().add(lblArrivalWindow);

		final JLabel label_1 = new JLabel("Arrival Window:");
		label_1.setBounds(588, 535, 145, 16);
		frame.getContentPane().add(label_1);


		final JLabel backgroundLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource(FBNConstants.INTERFACE_BACKGROUND)).getImage();
		backgroundLabel.setIcon(new ImageIcon(img));
		backgroundLabel.setBounds(0, 0, 1006, 608);
		frame.getContentPane().add(backgroundLabel);


		//Add action listener to search button
		searchFlights.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				doSearch(originText.getText(),
						destinationText.getText(),
						roundTripCheck.isSelected(),
						overnightCheck.isSelected(),            			 
						(String) flightClass.getSelectedItem(),
						(Integer) numberOfStops.getValue(),
						(Integer) minLayover.getValue(),
						(Integer) maxLayover.getValue(),
						departureDate.getCalendar(),
						returnDate.getCalendar());
			}
		});  
		//Add action listener to detail button
		detailFlights.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				long totalTripCost = 0;
				long arrivalTimeStamp = -1;
				long returnTimeStamp = -1;
				if ((!roundTripCheck.isSelected()) && (departTable.getSelectedRow()==-1)){
					FlyByNightPop.errorBox("Please select departing flight", "Error");
					return;
				}
				if ((roundTripCheck.isSelected()) && ((departTable.getSelectedRow()==-1) | (returnTable.getSelectedRow()==-1))){
					FlyByNightPop.errorBox("Please select both departing flight and returning flight", "Error");
					return;
				}

				String tripDetails = "Outbound: ";
				Trip departureTrip = flybynight.filterDepartingByTimeConstrains().getTrip(departTable.convertRowIndexToModel(departTable.getSelectedRow())); 	
				tripDetails += departureTrip.getTripDetails();
				arrivalTimeStamp = departureTrip.getArrivalTime();
				totalTripCost += departureTrip.getTotalCost(); 
				if(roundTripCheck.isSelected())
				{
					Trip returnTrip = flybynight.filterReturningByTimeConstrains().getTrip(returnTable.convertRowIndexToModel(returnTable.getSelectedRow())); 	
					tripDetails += "\n Inbound:" + returnTrip.getTripDetails();
					returnTimeStamp = returnTrip.getDepartureTime();
					totalTripCost += returnTrip.getTotalCost(); 
				}
				if(returnTimeStamp == -1 || returnTimeStamp > arrivalTimeStamp)
					FlyByNightPop.infoBox( "Here are the details of your selected trip: \n" + tripDetails + "\n(Outbound + Inbound) Total Trip Cost: U$D" + totalTripCost, "Flight Details");
				else
					FlyByNightPop.errorBox( "The Return Flight you selected is after (or to close to) the arrival time. This flight is not possible. ", "Invalid Flight");           	

			}
		});  		


		//Add action listener to reserve button
		researveFlights.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				long totalTripCost = 0;
				long arrivalTimeStamp = -1;
				long returnTimeStamp = -1;
				boolean reservationResult = false;
				Trip returnTrip = null;

				if ((!roundTripCheck.isSelected()) && (departTable.getSelectedRow()==-1)){
					FlyByNightPop.errorBox("Please select departing flight", "Error");
					return;
				}
				if ((roundTripCheck.isSelected()) && ((departTable.getSelectedRow()==-1) | (returnTable.getSelectedRow()==-1))){
					FlyByNightPop.errorBox("Please select both departing flight and returning flight", "Error");
					return;
				}
				String tripDetails = "Outbound: ";
				Trip departureTrip = flybynight.filterDepartingByTimeConstrains().getTrip(departTable.convertRowIndexToModel(departTable.getSelectedRow())); 	
				arrivalTimeStamp = departureTrip.getArrivalTime();
				tripDetails += departureTrip.getTripDetails();
				totalTripCost += departureTrip.getTotalCost(); 
				if(roundTripCheck.isSelected())
				{
					returnTrip = flybynight.filterReturningByTimeConstrains().getTrip(returnTable.convertRowIndexToModel(returnTable.getSelectedRow())); 	
					returnTimeStamp = returnTrip.getDepartureTime();
					tripDetails += "\n Inbound:" + returnTrip.getTripDetails();
					totalTripCost += returnTrip.getTotalCost(); 

				}

				if(returnTimeStamp == -1 || returnTimeStamp > arrivalTimeStamp + FBNConstants.MSINMINUTE * FBNConstants.LAYOVER_LOW)
				{	
					if(roundTripCheck.isSelected())
						reservationResult = flybynight.ReserveTrip(departureTrip,returnTrip, (String) flightClass.getSelectedItem());
					else
						reservationResult = flybynight.ReserveTrip(departureTrip,null, (String) flightClass.getSelectedItem());

					if(reservationResult) FlyByNightPop.infoBox( "Congratulations, you purchased the following trip: \n" + tripDetails + "\n(Outbound + Inbound) Total Trip Cost: U$D" + totalTripCost, "Confirmation!");
					JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
					addTripListToTable(new TripList(),returnTable);
					JTable departTable = (JTable) getComponentByName(frame,"departTable");
					addTripListToTable(new TripList(),departTable);	
				}
				else
					FlyByNightPop.errorBox( "The Return Flight you selected is after (or to close to) the arrival time. This flight is not possible. ", "Invalid Flight");           	

			}
		});  		
	}

	
	private void resetInterface()
	{
     			 
		JCheckBox roundTripCheck = (JCheckBox) getComponentByName(frame,"roundTripCheck");
		roundTripCheck.setSelected(false);
		JCheckBox overnightCheck = (JCheckBox) getComponentByName(frame,"overnightCheck");
		overnightCheck.setSelected(false);
		JTextField destinationText = (JTextField) getComponentByName(frame,"destinationText");
		destinationText.setText("");		
		JTextField originText = (JTextField) getComponentByName(frame,"originText");
		originText.setText("");
		JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
		addTripListToTable(new TripList(),returnTable);
		JTable departTable = (JTable) getComponentByName(frame,"departTable");
		addTripListToTable(new TripList(),departTable);	
	}
	
	/**
	 * Do search.
	 *
	 * @param originText the origin text
	 * @param destinationText the destination text
	 * @param roundTrip the round trip
	 * @param overnight the overnight
	 * @param cabinClass the cabin class
	 * @param maxStops the max stops
	 * @param minLayover the min layover
	 * @param maxLayover the max layover
	 * @param departureDate the departure date
	 * @param returnDate the return date
	 */
	private void doSearch(String originText,String destinationText, boolean roundTrip, boolean overnight,
			String cabinClass, int maxStops, int minLayover, int maxLayover, Calendar departureDate, Calendar returnDate){

		if (flybynight == null){
			flybynight = new FlyByNightAPI();
		}
	
		
		//Inputs Check
		if(originText.length() < 3){
			FlyByNightPop.errorBox("Missing Origin!", "Origin Check");
			return;
		}else if(destinationText.length() < 3){
			FlyByNightPop.errorBox("Missing Destination!", "Destination Check");
			return;
		}else if(departureDate==null){
			FlyByNightPop.errorBox("Missing Departure Date!", "Dates Check");
			return;
		}
		
		originText = originText.trim().substring(0, 3);
		destinationText = destinationText.trim().substring(0, 3);


		// Clear Cache before each search
		TripList departingTripList = new TripList();
		TripList returningTripList = new TripList();
		
		flybynight.setOrigin(originText.toUpperCase());
		flybynight.setDestination(destinationText.toUpperCase());
		flybynight.setRoundTrip(roundTrip);
		flybynight.setOvernight(overnight);	     
		flybynight.setCabinClass(cabinClass);
		flybynight.setMaxNumberOfStops(maxStops);
		flybynight.setMinLayover(minLayover); // In minutes 
		flybynight.setMaxLayover(maxLayover); // In minutes

		
		if(flybynight.isRoundTrip()) {
			//Return Dates Check
			if (returnDate==null){
				FlyByNightPop.errorBox("Missing Return Date!", "Dates Check");
				return;
			}else if (departureDate.compareTo(returnDate)>0){
				FlyByNightPop.errorBox("Wrong Dates Combination!", "Dates Check");
				return;
			}
			flybynight.setReturnDate(returnDate);
			flybynight.searchReturningTrips();
			returningTripList = flybynight.getReturningTrips();
			JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
			addTripListToTable(returningTripList,returnTable);
		}else{
			//Make sure that returning trip list is empty when searching for one-way ticket
			JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
			addTripListToTable(returningTripList,returnTable);
		}

		flybynight.setDepartureDate(departureDate);
		flybynight.searchDepartingTrips();
		departingTripList = flybynight.getDepartingTrips();
		JTable departTable = (JTable) getComponentByName(frame,"departTable");
		addTripListToTable(departingTripList,departTable);
		updateTablesWithFilteredResults();
		
		
		if (flybynight.isRoundTrip() && departingTripList.getTrips().isEmpty() && returningTripList.getTrips().isEmpty()){
			FlyByNightPop.errorBox("No Flights!", "Flights Check");
		}else if (flybynight.isRoundTrip() && departingTripList.getTrips().isEmpty() && !returningTripList.getTrips().isEmpty()){
			FlyByNightPop.errorBox("No Deparutre Flights!", "Flights Check");
		}else if (flybynight.isRoundTrip() && !departingTripList.getTrips().isEmpty() && returningTripList.getTrips().isEmpty()){
			FlyByNightPop.errorBox("No Return Flights!", "Flights Check");
		}else if (!flybynight.isRoundTrip() && departingTripList.getTrips().isEmpty()){
			FlyByNightPop.errorBox("No Flights!", "Flights Check");
		}
	}

	/**
	 * Update tables with filtered results.
	 */
	private void updateTablesWithFilteredResults(){

		if (flybynight == null){
			flybynight = new FlyByNightAPI();
		}
		// Clear Cache before each search
		TripList departingTripList = new TripList();
		TripList returningTripList = new TripList();
		
		if(flybynight.isRoundTrip()) {
			returningTripList = flybynight.filterReturningByTimeConstrains();
			if(returningTripList != null)
			{
				JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
				addTripListToTable(returningTripList,returnTable);
			}
		}else{
			if(returningTripList != null)
			{
				//Make sure that returning trip list is empty when searching for one-way ticket
				JTable returnTable = (JTable) getComponentByName(frame,"returnTable");
				addTripListToTable(returningTripList,returnTable);
			}
		}
		if(departingTripList != null)
		{
			departingTripList = flybynight.filterDepartingByTimeConstrains();
			JTable departTable = (JTable) getComponentByName(frame,"departTable");
			addTripListToTable(departingTripList,departTable);
		}
	}
	
	
	/**
	 * Add the trip list to table.
	 *
	 * @param tripList the trip list
	 * @param table the table
	 */
	private void addTripListToTable(TripList tripList, JTable table){
		((DefaultTableModel)table.getModel()).setRowCount(0);
		for (Trip trip:tripList.getTrips()){
			addTripToTable(trip,table);
		}
	}

	/**
	 * Add the trip to table.
	 *
	 * @param trip the trip
	 * @param table the table
	 */
	private void addTripToTable(Trip trip, JTable table){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{trip.getLocalDepartureTime(),trip.getLocalArrivalTime(),trip.getStops(),trip.getObjectTripDuration(),trip.getTotalCost(),trip.getCabinSwapFlag()});
	}

	/**
	 * Get the component by name.
	 *
	 * @param parent the parent
	 * @param name String The name of the component.
	 * @return The reference or null <br> <b>Need to be converted.</b>
	 */
	private Component getComponentByName(Component parent, String name){
		if (!(parent instanceof Container)){
			return null;
		}
		List<Component> components = getAllComponents((Container) parent);
		for (Component component:components){
			if (component.getName()==name){
				return component;
			}
		}
		return null;
	}

	/**
	 * Get the all components.
	 *
	 * @param c the c
	 * @return the all components
	 */
	private static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}
}
