package tedkubow.t0sample;

import org.junit.Ignore;
import org.junit.Test;

public class OrderBookTest {

	@Test
	@Ignore
	public void testBasicOrderBook() {
		OrderBook orderBook = OrderBook.getOrderBook();


		orderBook.buy(430, 5);
		orderBook.buy(430, 5);
		orderBook.buy(430.10, 10);

		orderBook.showBook();
		
		orderBook.sell(432, 2);
		orderBook.sell(433, 25);
		orderBook.sell(432.5, 8);
		orderBook.sell(433, 3);

		orderBook.showBook();
		
		orderBook.sell(420, 3);
		orderBook.showBook();		
	}
	
	@Test
	public void testRipTheBook() {
		OrderBook orderBook = OrderBook.getOrderBook();
		orderBook.sell(431, 5);
		orderBook.sell(432, 5);
		orderBook.sell(433, 15);
		
		orderBook.showBook();
		
		//rip all offers and set top bid at 500
		orderBook.buy(500, 100);
		
		orderBook.showBook();
		
	}

}
