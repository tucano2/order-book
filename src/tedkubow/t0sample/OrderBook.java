package tedkubow.t0sample;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderBook {

	private TreeMap<Double, AtomicInteger> bids = new TreeMap<Double, AtomicInteger>(new Comparator<Double>() {

		// reverse sort so best bids come first
		@Override
		public int compare(Double d1, Double d2) {
			return d1 > d2 ? -1 : d1 < d2 ? 1 : 0;
		}
	});
	
	private TreeMap<Double, AtomicInteger> asks = new TreeMap<Double, AtomicInteger>();//lowe

	public void buy(double price, int quanity) {

	}

	public void sell(double price, int quanity) {

	}

	public void showBook() {

	}

}
