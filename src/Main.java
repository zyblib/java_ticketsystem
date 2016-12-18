import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
	public static final int THREADCOUNT = 5;
	public static void main(String[] args) {
		TicketingDB tds = new TicketingDB();
		TicketThread[] thread = new TicketThread[THREADCOUNT];
		for (int i = 0; i < THREADCOUNT; ++i) {
			thread[i] = new TicketThread(tds);
		}
		long startime = System.nanoTime();
		for (int i = 0; i < THREADCOUNT; ++i) {
			thread[i].start();
		}
		for (int i = 0; i < THREADCOUNT; ++i) {
			try {
				thread[i].join();
			} catch (InterruptedException e) {
				System.out.println("the thread" + i + "  died");
			}
		}

		long totaltime = System.nanoTime() - startime;
		System.out.println("总共用时：" +totaltime);
		System.out.println("-------------end-----------------------");
	}
}

class TicketThread extends Thread {
	private static AtomicLong al=new AtomicLong(1);
	public enum ActionType {
		BUYTICKET, QUERYTICKET, REFUNDTICKET
	}
	//用于存储已经放出的票
    private ConcurrentLinkedQueue<Ticket> boughttickets;
	private int invokecount;
	private TicketingDB db;

	public TicketThread(TicketingDB db, int invokecount) {
		this.invokecount = invokecount;
		this.db = db;
		boughttickets=new ConcurrentLinkedQueue<Ticket>();
	}

	public TicketThread(TicketingDB db) {
		this(db, 10000);
	}

	@Override
	public void run() {
		int router;
		int departure;
		int arrival;
		for (int i = 0; i < invokecount; ++i) {
			switch (getActionType()) {
			case BUYTICKET:
				router = (int) (db.getRoutenum() * Math.random() + 1);
				departure = (int) (db.getStationnum() * Math.random() + 1);
				arrival = (int) ((db.getStationnum() - departure)
						* Math.random() + departure + 1);
				Ticket buyticket=db.buyTicket("passenger" + invokecount, router, departure,
						arrival);
				if(buyticket != null){
					//System.out.println("begin to offer ticket");
					boughttickets.offer(buyticket);
				}
				//System.out.println("buy ticket---------------");
				//System.out.println(buyticket);
				//System.out.println("buy ticket---------------");
				break;
			case QUERYTICKET:
				router = (int) (db.getRoutenum() * Math.random() + 1);
				departure = (int) (db.getStationnum() * Math.random() + 1);
				arrival = (int) ((db.getStationnum() - departure)
						* Math.random() + departure + 1);
				int count= db.inquiry(router, departure, arrival);
			   // System.out.println("inquirycount"+count);
				break;
			case REFUNDTICKET:
				//System.out.println("begin to poll ticket");
				Ticket refundticket = boughttickets.poll();
				if(refundticket !=null){
				boolean success = db.refundTicket(refundticket);
			   // System.out.println("refundticket result: " +success);
				}
				break;

			default:
				throw new RuntimeException(
						"can achive the default on getActiontype switch");
			}
		}
		System.out.println("thead over which id is  "+ al.getAndIncrement());
		//System.out.println("thead over which id is  "+ this.getId());
	}

	/**
	 * 使得60%查询余票，30%购票，10%退票
	 * 
	 * @return
	 */
	private ActionType getActionType() {
		int random = (int) (Math.random() * 10);
		if (random < 6)
			return ActionType.QUERYTICKET;
		if (random > 8)
			return ActionType.REFUNDTICKET;
		return ActionType.BUYTICKET;
	}
}


