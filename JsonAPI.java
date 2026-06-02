import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class JsonAPI {
    static List<String> names = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/hello", new HelloHandler());
        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/createID", new CreateIDHandler());
        server.createContext("/api/InsertNametoDB", new InsertNametoDBHandler());
        server.createContext("/api/authorName", new getAuthorHandler());
        System.out.println("Server running on port " + port);
        server.start();
    }

    static class InsertNametoDBHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("POST")) {
                String name = new String(exchange.getRequestBody().readAllBytes());
                names.add(name);
                String response = "{\"message\":\"Name added to database\",\"name\":\"" + name + "\",\"Total Entry\":"
                        + names.size() + "}";
                sendJsonResponse(exchange, 200, response);
            } else {
                String response = "{\"error\":\"Method not allowed\"}";
                sendJsonResponse(exchange, 405, response);
            }
        }
    }

    static class getAuthorHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                sendJsonResponse(exchange, 200, "Created by Tharvesh");
            } else {
                String response = "{\"error\":\"Method not allowed\"}";
                sendJsonResponse(exchange, 405, response);
            }
        }
    }

    static class CreateIDHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("POST")) {
                String response = "{\"id\":\"12345\"}";
                sendJsonResponse(exchange, 200, response);
            } else {
                String response = "{\"error\":\"Method not allowed\"}";
                sendJsonResponse(exchange, 405, response);
            }
        }
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                String response = "{\"message\":\"Hello from Java\"}";
                sendJsonResponse(exchange, 200, response);
            } else {
                String response = "{\"error\":\"Method not allowed\"}";
                sendJsonResponse(exchange, 405, response);
            }
        }
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equals("GET")) {
                String response = "{\"status\":\"UP\",\"database\":\"OFFLINE\"}";
                sendJsonResponse(exchange, 200, response);
            } else {
                String response = "{\"error\":\"Method not allowed\"}";
                sendJsonResponse(exchange, 405, response);
            }
        }
    }

    private static void sendJsonResponse(HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        exchange.sendResponseHeaders(statusCode, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
