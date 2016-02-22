package cs509Blizzard.FBNUtilities;

public class ComparableTime implements Comparable {

	private long time = 0;
	
	public void setTime(long t){
		time = t;
	}
	public long getTime(){
		return time;
	}
	public ComparableTime(long t){
		this.time = t;
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if (arg0 instanceof ComparableTime){
			if (((ComparableTime)arg0).getTime()>this.time){
				return -1;
			}else if (((ComparableTime)arg0).getTime()==this.time){
				return 0;
			}else{
				return 1;
			}
		}else{
			
		}
		return 0;
	}
	@Override 
	public String toString(){
		long seconds = this.time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		String time = ";";
		if(days > 0)
			time = days + "d:" + hours % 24 + "h:" + minutes % 60 + "m"; 	
		else 
			time = hours % 24 + "h:" + minutes % 60 + "m"; 

		return time;
	}

//	public static void main(String [ ] args){
//		ComparableTime a = new ComparableTime(12);
//		ComparableTime b = new ComparableTime(13);
//		if (a>b){
//			System.out.println("a is bigger");
//		}else{
//			System.out.println("b is bigger'")
//		}
//	}
}
