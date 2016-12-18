
public class Ticket {
	/**
	 * tid 是车票编号
	 */
	long tid;
	/**
	 * passenger是乘客名字
	 */
	String passenger;
	/**
	 * route是列车车次
	 */
	int route;
	/**
	 * coach是车厢号
	 */
	int coach;
	/**
	 * seat是座位号
	 */
	int seat;
	/**
	 * departure是出发站编号
	 */
	int departure;
	/**
	 * arrival是到站编号
	 */
	int arrival;
	
	boolean validation=false;
    @Override
    public int hashCode() {
    	return (int) (tid*31^(tid>>32));
    }
    @Override
    public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Ticket) {
			Ticket ticket = (Ticket) obj;
			return passenger != null && passenger.equals(ticket.passenger)
					&& ticket.departure == departure
					&& ticket.arrival == arrival && ticket.coach == coach
					&& ticket.seat == seat;

		}
    		
    	return false;
    }
	@Override
	public String toString() {
		return "Ticket [tid=" + tid + ", passenger=" + passenger + ", route="
				+ route + ", coach=" + coach + ", seat=" + seat
				+ ", departure=" + departure + ", arrival=" + arrival + "]";
	}
}
