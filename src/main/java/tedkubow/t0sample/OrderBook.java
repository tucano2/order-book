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
		sweepBook(limitPrice, quantity, offers, true);
	}

	public void sell(double limitPrice, int quantity) {
		sweepBook(limitPrice, quantity, bids, false);
	}

	private void sweepBook(double limitPrice, int quantity, TreeMap<Double, AtomicInteger> book, boolean isBuying) {

		Entry<Double, AtomicInteger> topOfBook = book.firstEntry();

		if (topOfBook != null) {//check book is empty
			Double topOfBookPrice = topOfBook.getKey();
			AtomicInteger topOfBookQuantity = topOfBook.getValue();

			if ((isBuying && topOfBookPrice <= limitPrice) || (!isBuying && topOfBookPrice >= limitPrice)) {//check order is not marketable

				int remainingShares = topOfBookQuantity.addAndGet(quantity * -1);

				if (remainingShares < 0) {//order partially filled, all liquidity taken at current level
					book.pollFirstEntry();
					sweepBook(limitPrice, Math.abs(remainingShares), book, isBuying);
				}

				if (remainingShares == 0) {//order fully filled, all liquidity taken at current level
					book.pollFirstEntry();
				}

				if (remainingShares > 0) {//order fully filled, still liquidity at level
					System.out.println((isBuying?"Bought " : "Sold ") + quantity + " at " + topOfBookPrice);
				}
			} else {
				addOrderToBook(limitPrice, quantity, isBuying ? bids : offers);
			}
		} else {
			addOrderToBook(limitPrice, quantity, isBuying ? bids : offers);
		}
	}

	private void addOrderToBook(double limitPrice, int quantity, TreeMap<Double, AtomicInteger> book) {
		AtomicInteger existingQuantity = book.putIfAbsent(limitPrice, new AtomicInteger(quantity));
		if (existingQuantity != null) {// price level already exists so accumulate quantity
			existingQuantity.addAndGet(quantity);
		}
	}

	public void showBook() {
		System.out.println("bids:" + bids);
		System.out.println("offers:" + offers);
	}
	
	public static OrderBook getOrderBook() {
		return theOrderBook;
	}

	// singelton
	private OrderBook() {

	}

}
