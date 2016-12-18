import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
public class Router {
	//火车的车厢数量
	private int coachnum;
	//每节车厢的座位数
	private int seatnum;
	//一共经过的车站数
	private int station;
	Seat[][][] seatsold;
	private ConcurrentHashMap<Ticket, AtomicBoolean> validationManager;
	private AtomicLong IDMaker;
	public Router(int coachnum, int seatnum, int station) {
		this.coachnum = coachnum;
		this.seatnum = seatnum;
		this.station = station;
		IDMaker = new AtomicLong(0l);
		initRouter();
	}
	private void initRouter() {
      seatsold=new Seat[station+1][coachnum+1][seatnum+1];
      for(int i=1;i<=station;++i)
    	  for(int j=1;j<=coachnum;++j)
    		  for(int k=1;k<=seatnum;++k){
    			  seatsold[i][j][k] =new Seat();
    		  }
    		  
    	  
      
      validationManager=new ConcurrentHashMap<Ticket,AtomicBoolean>();
	}
	/**
	 * 
	 * @param departure
	 * @param arrival
	 * @return 是否存在票
	 * 查找始发站的每一个车厢
	 */
	public int getTicketCount(int departure, int arrival) {
		if (departure < 1 || departure > station || arrival > station
				|| arrival < 1 || arrival < departure)
			return 0;
		int count = 0;
		for (int j = 1; j <= coachnum; ++j) {
			for (int i = 1; i <= seatnum; ++i) {
				if (!seatsold[departure][j][i].isOccupy()) {
					int k = departure + 1;
					for (; k < arrival; ++k) {
						if (!seatsold[k][j][i].isOccupy())
							break;
					}
					if (k == arrival)
						++count;
				}

			}
		}
		return count;
	}

	public Ticket getTicket(Ticket ticket) {
		int departure=ticket.departure;
		int arrival=ticket.arrival;
		if (departure < 1 || departure > station || arrival > station
				|| arrival < 1 || arrival < departure)
			return ticket;
		int count = getTicketCount(departure, arrival);
		if (count < 1)
			return ticket;
		int startcouch = (int) (coachnum* Math.random())+ 1;
		int startseat = (int) (seatnum* Math.random())+ 1;
		boolean continuecouch=true;
		boolean continueseat=true;
		for(int j=startcouch;continuecouch;j=(++j)%(coachnum+1)){
			if (j == (startcouch - 1))
				continuecouch = false;
			if (j == 0)
				++j;
			for(int i=startseat;continueseat;i=(++i)%(seatnum+1)){
				if (i == (startseat - 1))
					continueseat = false;
				if (i == 0)
					++i;
				//System.out.println("startcouch"+j +"startseat"+i);
				if (!seatsold[departure][j][i].isOccupy()) {
				//	System.out.println("stop in"+87);
					int k = departure + 1;
				//	System.out.println("stop in"+88);
					for (; k < arrival; ++k) {
						//System.out.println("stop in"+89);
						if (seatsold[k][j][i].isOccupy())
						//	System.out.println("stop in"+90);
							break;
					}
				//	System.out.println("stop in"+91);
					if (k == arrival) {
						//System.out.println("stop in"+92);
						boolean isChange = false;
					//	System.out.println("beging to get into syn");
						synchronized (this) {
							for (int t = departure; t < arrival; ++t) {
								if (seatsold[t][j][i].isOccupy()) {
									isChange = true;
									break;
								}
							}
							if (!isChange) {
								for (int t = departure; t < arrival; ++t) {
									seatsold[t][j][i].setOccupy(true);
								}
							}
						}
						//System.out.println("out to get into syn");
						if (!isChange) {
							//System.out.println("being to get id -------");
							ticket.tid = getTicketId();
							ticket.coach = j;
							ticket.seat = i;
							ticket.validation = true;
							//System.out.println("beging to put map----");
							validationManager.put(ticket, new AtomicBoolean(
									true));
							return ticket;
						}

					}
				}

			}
		}

		return ticket;
	}
	private long getTicketId() {
  		return IDMaker.getAndIncrement();
}
	public boolean refundTicket(Ticket ticket) {
		if(ticket ==null)
			return false;
		AtomicBoolean stat =validationManager.get(ticket);
		if(stat == null){
			return false;
		}
		if(stat.compareAndSet(true, false)){
			for(int i=ticket.departure;i<ticket.arrival;++i){
				seatsold[i][ticket.coach][ticket.seat].setOccupy(false);
			}
			validationManager.remove(ticket);
			return true;
		}
		return false;
	}
}
