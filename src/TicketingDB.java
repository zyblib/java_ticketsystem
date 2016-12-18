
public class TicketingDB implements TicketingSystem {
	// ���ε�����
	private int routenum;
	// ÿ���г�������Ŀ
	private int coachnum;
	// ÿ�ڳ������λ��
	private int seatnum;
	// ����վ���������Ĭ����10��������ʼ��վ���յ�վ
	private int stationnum;
	
	
	private Router[] routers;
	
	public TicketingDB(int routenum, int coachnum, int seatnum, int stationnum) {
		this.routenum = routenum;
		this.coachnum = coachnum;
		this.seatnum = seatnum;
		this.stationnum = stationnum;
		initDB();
	}
    private void initDB() {
         	routers=new Router[routenum+1];
         	for(int i=1;i<=routenum;++i){
         		routers[i]=new Router(coachnum,seatnum,stationnum);
         	}
	}
	public TicketingDB(){
    	this(5,8,100,10);
    }

	@Override
	public Ticket buyTicket(String passenger, int route, int departure,
			int arrival) {
		Ticket ticket = new Ticket();
		ticket.passenger=passenger;
		ticket.route=route;
		ticket.departure=departure;
		ticket.arrival=arrival;
		
		if (route < 1 || route > routenum || departure < 1
				|| arrival > stationnum || departure > arrival)
			return null;
		ticket = routers[route].getTicket(ticket);
		if (ticket.validation) {
			return ticket;
		} else {
			return null;
		}
	}

	@Override
	public int inquiry(int route, int departure, int arrival) {
		if(route<1 || route>routenum)
			return 0;
		return routers[route].getTicketCount(departure, arrival);
	}

	@Override
	public boolean refundTicket(Ticket ticket) {
			return routers[ticket.route].refundTicket(ticket);
	}
	public int getRoutenum() {
		return routenum;
	}
	public int getStationnum() {
		return stationnum;
	}

}
