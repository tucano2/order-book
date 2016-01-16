package tedkubow.t0sample;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class OrderBookRestWrapper {

	private OrderBook orderBook;
	Gson gson = new GsonBuilder().create();

	public OrderBookRestWrapper() {
		orderBook = OrderBook.getOrderBook();

		port(3000);
		get("/book", (req, res) -> gson.toJson(book()));
		post("/buy", "application/json", (req, res) -> {
			
			String body = req.body();			
			Gson gson = new Gson();		
			QuantityAndPrice payload = gson.fromJson(body, QuantityAndPrice.class);
			orderBook.buy(payload.prc, payload.qty);
			return "";
		});
		post("/sell", "application/json", (req, res) -> {			
			String body = req.body();
			Gson gson = new Gson();			
			QuantityAndPrice payload = gson.fromJson(body, QuantityAndPrice.class);
			orderBook.sell(payload.prc, payload.qty);
			return "";
		});
	}

	public void buy(double limitPrice, int quantity) {
		orderBook.buy(limitPrice, quantity);
	}

	public void sell(double limitPrice, int quantity) {
		orderBook.sell(limitPrice, quantity);
	}

	public Results book() {

		Map<String, TreeMap<Double, AtomicInteger>> internalBook = orderBook.showBook();

		// convert Map.Entry to QuantityAndPrice so GSON will serialize with
		// desired field names
		List<QuantityAndPrice> buysList = internalBook.get("buys").entrySet().stream()
				.map(e -> new QuantityAndPrice(e.getValue().get(), e.getKey())).collect(Collectors.toList());
		List<QuantityAndPrice> sellsList = internalBook.get("sells").entrySet().stream()
				.map(e -> new QuantityAndPrice(e.getValue().get(), e.getKey())).collect(Collectors.toList());

		return new Results(buysList, sellsList);

	}

	public static void main(String[] args) {
		new OrderBookRestWrapper();
		System.out.println("server running...");
	}

	// JSON wrapper
	static class Results {
		final BuysAndSells results;

		public Results(List<QuantityAndPrice> buys, List<QuantityAndPrice> sells) {
			this.results = new BuysAndSells(buys, sells);
		}

		public BuysAndSells getResults() {
			return results;
		}

		class BuysAndSells {
			final List<QuantityAndPrice> buys;
			final List<QuantityAndPrice> sells;

			public BuysAndSells(List<QuantityAndPrice> buys, List<QuantityAndPrice> sells) {
				this.buys = buys;
				this.sells = sells;
			}

			public List<QuantityAndPrice> getBuys() {
				return buys;
			}

			public List<QuantityAndPrice> getSells() {
				return sells;
			}
		}
	}

	// JSON wrapper
	static class QuantityAndPrice {
		Integer qty;
		Double prc;

		public QuantityAndPrice(Integer qty, Double prc) {
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
}
