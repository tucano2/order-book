package tedkubow.t0sample;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderBook {

	private static OrderBook theOrderBook = new OrderBook();

	private final NavigableMap<Double, AtomicInteger> offers = new ConcurrentSkipListMap<>();
	private final NavigableMap<Double, AtomicInteger> bids = new ConcurrentSkipListMap<>(new Comparator<Double>() {
		// reverse sort so best bids come first
		@Override
		public int compare(Double d1, Double d2) {
			return d1 > d2 ? -1 : d1 < d2 ? 1 : 0;
		}
	});
	private final Map<String, NavigableMap<Double, AtomicInteger>> fullBook = new HashMap<>(3, 100);

	public void buy(double limitPrice, int quantity) {
		sweepBook(limitPrice, quantity, true);
	}

	public void sell(double limitPrice, int quantity) {
		sweepBook(limitPrice, quantity, false);
	}

	public Map<String, NavigableMap<Double, AtomicInteger>> showBook() {
		return fullBook;
	}

	private void sweepBook(double limitPrice, int quantity, boolean isBuying) {

		NavigableMap<Double, AtomicInteger> targetBook = isBuying ? offers : bids;
		Entry<Double, AtomicInteger> topOfBook = targetBook.firstEntry();

		if (topOfBook != null) {// check book is empty
			Double topOfBookPrice = topOfBook.getKey();
			AtomicInteger topOfBookQuantity = topOfBook.getValue();

			if ((isBuying && topOfBookPrice <= limitPrice) || (!isBuying && topOfBookPrice >= limitPrice)) {// check order is marketable

				int remainingShares = topOfBookQuantity.addAndGet(quantity * -1);

				if (remainingShares < 0) {// order partially filled, all
											// liquidity taken at current level
					targetBook.pollFirstEntry();
					sweepBook(limitPrice, Math.abs(remainingShares), isBuying);
					logTrade(isBuying, quantity + remainingShares, topOfBookPrice);
				}

				if (remainingShares == 0) {// order fully filled, all liquidity
											// taken at current level
					targetBook.pollFirstEntry();
					logTrade(isBuying, quantity, topOfBookPrice);
				}

				if (remainingShares > 0) {// order fully filled, still liquidity
											// at level
					logTrade(isBuying, quantity, topOfBookPrice);
				}
			} else {
				addOrderToBook(limitPrice, quantity, isBuying ? bids : offers);
			}
		} else {
			addOrderToBook(limitPrice, quantity, isBuying ? bids : offers);
		}
	}

	private void logTrade(boolean isBuying, int quantity, Double tradePrice) {
		System.out.println((isBuying ? "Bought " : "Sold ") + quantity + " at " + tradePrice);
	}

	private void addOrderToBook(double limitPrice, int quantity, NavigableMap<Double, AtomicInteger> book) {
		AtomicInteger existingQuantity = book.putIfAbsent(limitPrice, new AtomicInteger(quantity));
		if (existingQuantity != null) {// price level already exists so accumulate quantity
			existingQuantity.addAndGet(quantity);
		}
	}

	public static OrderBook getOrderBook() {
		return theOrderBook;
	}

	// singelton
	private OrderBook() {
		fullBook.put("buys", bids);
		fullBook.put("sells", offers);
	}

}
