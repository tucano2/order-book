package tedkubow.t0sample;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class OrderBookRestWrapper {

	private OrderBook orderBook;

	public OrderBookRestWrapper() {
		orderBook = OrderBook.getOrderBook();

		port(3000);
		get("/book", (req, res) -> book());
		post("/buy", (req, res) -> {
			String body = req.body();
			Gson gson = new Gson();
			Payload payload = gson.fromJson(body, Payload.class);
			orderBook.buy(payload.prc, payload.qty);
			return "";
		});
		// post("/sell", (req, res) -> sell());
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

	static class Payload {
		Integer qty;
		Double prc;

		public Payload(Integer qty, Double prc) {
			super();
			this.qty = qty;
			this.prc = prc;
		}

		public Integer getQty() {
			return qty;
		}

		public void setQty(Integer qty) {
			this.qty = qty;
		}

		public Double getPrc() {
			return prc;
		}

		public void setPrc(Double prc) {
			this.prc = prc;
		}

	}
	
	static class 
}
