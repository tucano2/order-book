package tedkubow.t0sample;

public class OrderBookRestWrapper {
	
	private OrderBook orderBook;
	
	public OrderBookRestWrapper() {
		orderBook = OrderBook.getOrderBook();		
	}
	
	
	public static void main(String[] args) {
		OrderBookRestWrapper restWrapper = new OrderBookRestWrapper();				
	}
	
	
	public void buy(double limitPrice, int quantity) {
		orderBook.buy(limitPrice, quantity);
	}
	
	public void sell(double limitPrice, int quantity) {
		orderBook.sell(limitPrice, quantity);
	}
	
	
	

}
