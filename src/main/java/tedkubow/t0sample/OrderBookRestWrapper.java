package tedkubow.t0sample;

import static spark.Spark.get;

import java.util.ArrayList;
import java.util.List;

public class OrderBookRestWrapper {

	private OrderBook orderBook;

	public OrderBookRestWrapper() {
		orderBook = OrderBook.getOrderBook();

		get("/hello", (req, res) -> "Hello World");
	}

	public void buy(double limitPrice, int quantity) {
		orderBook.buy(limitPrice, quantity);
	}

	public void sell(double limitPrice, int quantity) {
		orderBook.sell(limitPrice, quantity);
	}

	public List<String> book() {
		ArrayList<String> l = new ArrayList<String>();
		l.add("ted test");
		return l;
	}

	public static void main(String[] args) {

		OrderBookRestWrapper restWrapper = new OrderBookRestWrapper();
		System.out.println("running...");

	}
}
