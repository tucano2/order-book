package tedkubow.t0sample;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderBook {

	private static OrderBook theOrderBook = new OrderBook();

	private TreeMap<Double, AtomicInteger> offers = new TreeMap<Double, AtomicInteger>();
	private TreeMap<Double, AtomicInteger> bids = new TreeMap<Double, AtomicInteger>(new Comparator<Double>() {

		// reverse sort so best bids come first
		@Override
		public int compare(Double d1, Double d2) {
			return d1 > d2 ? -1 : d1 < d2 ? 1 : 0;
		}
	});

	public void buy(double limitPrice, int quantity) {
		sweepOffers(limitPrice, quantity);
	}

	public void sell(double limitPrice, int quantity) {
		sweepBids(limitPrice, quantity);
	}

	private void sweepOffers(double limitPrice, int quantity) {
		sweepBook(limitPrice, quantity, offers, true);
	}

	private void sweepBids(double limitPrice, int quantity) {
		sweepBook(limitPrice, quantity, bids, false);
	}

	private void sweepBook(double limitPrice, int quantity, TreeMap<Double, AtomicInteger> book, boolean isBuying) {
		Entry<Double, AtomicInteger> topOfBook = book.firstEntry();

		if (topOfBook != null) {
			Double topOfBookPrice = topOfBook.getKey();
			AtomicInteger topOfBookQuantity = topOfBook.getValue();

			if ((isBuying && topOfBookPrice <= limitPrice) || topOfBookPrice >= limitPrice) {

				int remainingShares = topOfBookQuantity.addAndGet(quantity * -1);

				if (remainingShares < 0) {
					book.pollFirstEntry();
					sweepBook(limitPrice, Math.abs(remainingShares), book, isBuying);
				}

				if (remainingShares == 0) {
					book.pollFirstEntry();
				}

				if (remainingShares > 0) {
					addOrderToBook(limitPrice, remainingShares, book);
				}
			}
		} else {
			addOrderToBook(limitPrice, quantity, book);
		}
	}

	private void addOrderToBook(double limitPrice, int quantity, TreeMap<Double, AtomicInteger> book) {
		AtomicInteger existingQuantity = book.putIfAbsent(limitPrice, new AtomicInteger(quantity));
		if (existingQuantity != null) {
			existingQuantity.addAndGet(quantity);// accumulate quantity at level
		}
	}

	public void showBook() {

	}

	public double getBestPrice(TreeMap<Double, AtomicInteger> book) {
		return book.firstEntry().getKey();
	}

	public double getBestBid() {
		return getBestPrice(bids);
	}

	public double getBestOffer() {
		return getBestPrice(offers);
	}

	public static OrderBook getOrderBook() {
		return theOrderBook;
	}

	// singelton
	private OrderBook() {
		// seed books
		bids.put(0D, new AtomicInteger(0));
		offers.put(Double.MAX_VALUE, new AtomicInteger(0));
	}

}
