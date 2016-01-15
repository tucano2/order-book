package tedkubow.t0sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@Path("/test")
public class OrderBookRestWrapper {

	private OrderBook orderBook;

	public OrderBookRestWrapper() {
		orderBook = OrderBook.getOrderBook();
	}

	public static void main(String[] args) {

		try {
			createHttpServer();
		} catch (IOException e) {
			throw new IllegalStateException("Can't start http server", e);
		}
		OrderBookRestWrapper restWrapper = new OrderBookRestWrapper();
		System.out.println("running...");

	}

	@POST
	public void buy(double limitPrice, int quantity) {
		orderBook.buy(limitPrice, quantity);
	}

	@POST
	public void sell(double limitPrice, int quantity) {
		orderBook.sell(limitPrice, quantity);
	}

	@GET
	public List<String> book() {
		ArrayList<String> l = new ArrayList<String>();
		l.add("ted test");
		return l;
	}

	// REST server specific details
	private static HttpServer createHttpServer() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
		server.createContext("/test", new HttpHandler() {

			public void handle(HttpExchange arg0) throws IOException {
				System.out.println("got httpexchange..." + arg0);

			}
		});
		server.setExecutor(null); // creates a default executor
		server.start();
		return server;
	}

}
