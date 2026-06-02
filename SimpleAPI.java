import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleAPI {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/hello", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                if (exchange.getRequestMethod().equals("GET")) {

                    String response = "{\"message\":\"Hello from Java\"}";

                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                } else {

                    String response = "{\"error\":\"Method not allowed\"}";

                    exchange.sendResponseHeaders(405, response.length());

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        server.createContext("/api/status", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                if (exchange.getRequestMethod().equals("GET")) {

                    String response = "{\"status\":\"UP\",\"database\":\"OFFLINE\"}";

                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                } else {

                    String response = "{\"error\":\"Method not allowed\"}";

                    exchange.sendResponseHeaders(405, response.length());

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        System.out.println("Server is running on port 8080");
        server.start();
    }
}
