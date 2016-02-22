package cs509Blizzard.FBNUtilities;


/**
 * The Class FlightListKey is used This is a compound hashing object to store flights in a hash. 
 * A bit of an overkill, but it is the correct way of hashing on multiple criteria. 
 * 
 * @author 	   Ermal Toto
 */
public class FlightListKey {
	
	/** The origin airport code (3 - letter code) for which flights needs to be looked up */
	String origin;
	
	/** The year of the search.  */
	int year;
	
	/** The month of the search. */
	int month;
	
	/** The day of the search.  */
	int day;
	
	/**
	 * Constructor that instantiates a new flight list key based on the  given parameters. 
	 *
	 * @param origin the origin
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 */
	public FlightListKey(String origin,int year, int month, int day)
	{
		this.origin = origin;
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + year;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightListKey other = (FlightListKey) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	
}