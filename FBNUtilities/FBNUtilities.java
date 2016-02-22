package cs509Blizzard.FBNUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FBNUtilities {

	/**
	 * Gets the day of the month when give a local date in a string format. THe date string
	 * is of the following format: MMMM dd HH:mm z this method first converts it to: MMMM dd HH:mm
	 * then pulls out the day of the month (regardless of timezone). 
	 *
	 * @param dateString in a MMMM dd HH:mm z format
	 * @return the day of the month
	 */

	public static int getDayFromString(String dateString)
	{
		SimpleDateFormat format = new SimpleDateFormat(FBNConstants.LOCAL_DATE_FORMAT_NO_TIMEZONE);
		Date date = null;

		try {
			date = format.parse(dateString.substring(0, dateString.length() - 3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_MONTH);	
	}

	/**
	 * Gets the hour of the day in a 24h formwat when give a local date in a string format. THe date string
	 * is of the following format: MMMM dd HH:mm z this method first converts it to: MMMM dd HH:mm
	 * then pulls out the hour of the day (regardless of timezone). 
	 *
	 * @param dateString in a MMMM dd HH:mm z format
	 * @return hour of the day
	 */

	public static int getHourFromString(String dateString)
	{
		SimpleDateFormat format = new SimpleDateFormat(FBNConstants.LOCAL_DATE_FORMAT_NO_TIMEZONE);
		Date date = null;

		try {
			date = format.parse(dateString.substring(0, dateString.length() - 3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.HOUR_OF_DAY);	
	}	
}
