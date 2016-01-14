package tedkubow.t0sample;

import org.junit.Test;

public class OrderBookTest {

	@Test
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

}
