package tedkubow.t0sample;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import tedkubow.t0sample.OrderBookRestWrapper.QuantityAndPrice;

public class GSONTest {

	Type type = new TypeToken<Entry<Double, AtomicInteger>>() {
	}.getType();

	@Test
	public void testGSONOrderBook() {
		/*
		 * Type type = new TypeToken<Map<String, TreeMap<Double,
		 * AtomicInteger>>>() { }.getType(); JsonSerializer<Map<String,
		 * TreeMap<Double, AtomicInteger>>> bookEntrySerializer = new
		 * JsonSerializer<Map<String, TreeMap<Double, AtomicInteger>>>() {
		 * 
		 * @Override public JsonElement serialize(Map<String, TreeMap<Double,
		 * AtomicInteger>> arg0, Type arg1, JsonSerializationContext arg2) { //
		 * TODO Auto-generated method stub return new JsonPrimitive("HI"); }
		 * 
		 * };
		 * 
		 */

		JsonSerializer<Entry<Double, AtomicInteger>> bookEntrySerializer = new JsonSerializer<Entry<Double, AtomicInteger>>() {

			@Override
			public JsonElement serialize(Entry<Double, AtomicInteger> arg0, Type arg1, JsonSerializationContext arg2) {
				return new JsonPrimitive("HIHI");
			}
		};

		JsonSerializer<AtomicInteger> quantitySerializer = new JsonSerializer<AtomicInteger>() {
			@Override
			public JsonElement serialize(AtomicInteger atomicInt, Type arg1, JsonSerializationContext arg2) {
				return new JsonPrimitive("qty:" + atomicInt.get());
			}
		};

		JsonSerializer<Double> priceSerializer = new JsonSerializer<Double>() {
			@Override
			public JsonElement serialize(Double arg0, Type arg1, JsonSerializationContext arg2) {
				return new JsonPrimitive("double");
			}
		};

		Gson gson = new GsonBuilder().create();

		OrderBook orderBook = OrderBook.getOrderBook();
		orderBook.buy(350, 10);
		orderBook.buy(300, 25);

		orderBook.sell(400, 10);
		orderBook.sell(425, 25);

		Map<String, NavigableMap<Double, AtomicInteger>> showBook = orderBook.showBook();
		Map<String, List<QuantityAndPrice>> bookToPublish = new HashMap<>(2);

		//convert Entry to QuantityAndPrice so GSON will serialize with desired field names
		List<QuantityAndPrice> buysList = showBook.get("buys").entrySet().stream()
				.map(e -> new QuantityAndPrice(e.getValue().get(), e.getKey())).collect(Collectors.toList());
		List<QuantityAndPrice> sellsList = showBook.get("sells").entrySet().stream()
				.map(e -> new QuantityAndPrice(e.getValue().get(), e.getKey())).collect(Collectors.toList());

		bookToPublish.put("buys", buysList);
		bookToPublish.put("sells", sellsList);

		System.out.println(gson.toJson(bookToPublish));
	}

	@Test
	public void testGSONObjects() {
		Gson gson = new Gson();

		OrderBookRestWrapper.QuantityAndPrice p = new QuantityAndPrice(5, 5.4);

		System.out.println(gson.toJson(p));

		OrderBookRestWrapper.QuantityAndPrice p1 = new QuantityAndPrice(15, 5.4);
		OrderBookRestWrapper.QuantityAndPrice p2 = new QuantityAndPrice(6, 323.4);

		List<QuantityAndPrice> qps = new ArrayList<OrderBookRestWrapper.QuantityAndPrice>();
		qps.add(p1);
		qps.add(p2);

		System.out.println(gson.toJson(qps));
	}

	@Test
	public void testGSONPrimitives() {
		Gson gson = new Gson();

		int[] ints = { 5, 77 };

		System.out.println(gson.toJson(ints));

		List<Integer> bigInts = new ArrayList<Integer>();
		bigInts.add(100);
		bigInts.add(25);

		System.out.println(gson.toJson(bigInts));

	}

}
