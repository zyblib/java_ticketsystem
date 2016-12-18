
public class Ticket {
	/**
	 * tid �ǳ�Ʊ���
	 */
	long tid;
	/**
	 * passenger�ǳ˿�����
	 */
	String passenger;
	/**
	 * route���г�����
	 */
	int route;
	/**
	 * coach�ǳ����
	 */
	int coach;
	/**
	 * seat����λ��
	 */
	int seat;
	/**
	 * departure�ǳ���վ���
	 */
	int departure;
	/**
	 * arrival�ǵ�վ���
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
