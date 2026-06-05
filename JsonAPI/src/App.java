package JsonAPI.src;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    static List<String> names = new ArrayList<>();
    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/hello", new HelloHandler());
        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/createID", new CreateIDHandler());
        server.createContext("/api/InsertNametoDB", new InsertNametoDBHandler());
        server.createContext("/api/authorName", new GetAuthorHandler());

        System.out.println("Server running on port " + port);

        server.start();
    }

    static class InsertNametoDBHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {

                if (!exchange.getRequestMethod().equals("POST")) {
                    throw new ApiException("Method not allowed", 405);
                }

                NameRequest request =
                        mapper.readValue(exchange.getRequestBody(), NameRequest.class);

                if (request.getName() == null ||
                        request.getName().trim().isEmpty()) {

                    throw new ApiException("Name cannot be empty", 400);
                }

                names.add(request.getName());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Name added to database");
                response.put("name", request.getName());
                response.put("totalEntry", names.size());

                sendJsonResponse(exchange, 200,
                        mapper.writeValueAsString(response));

            } catch (ApiException e) {

                Map<String, Object> error = new HashMap<>();
                error.put("error", e.getMessage());

                sendJsonResponse(exchange,
                        e.getStatusCode(),
                        mapper.writeValueAsString(error));
            }
        }
    }

    static class GetAuthorHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (exchange.getRequestMethod().equals("GET")) {

                Map<String, String> response = new HashMap<>();
                response.put("author", "Tharvesh");

                sendJsonResponse(exchange,
                        200,
                        mapper.writeValueAsString(response));

            } else {

                Map<String, String> response = new HashMap<>();
                response.put("error", "Method not allowed");

                sendJsonResponse(exchange,
                        405,
                        mapper.writeValueAsString(response));
            }
        }
    }

    static class CreateIDHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (exchange.getRequestMethod().equals("POST")) {

                Map<String, String> response = new HashMap<>();
                response.put("id", "12345");

                sendJsonResponse(exchange,
                        200,
                        mapper.writeValueAsString(response));

            } else {

                Map<String, String> response = new HashMap<>();
                response.put("error", "Method not allowed");

                sendJsonResponse(exchange,
                        405,
                        mapper.writeValueAsString(response));
            }
        }
    }

    static class HelloHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (exchange.getRequestMethod().equals("GET")) {

                Map<String, String> response = new HashMap<>();
                response.put("message", "Hello from Java");

                sendJsonResponse(exchange,
                        200,
                        mapper.writeValueAsString(response));

            } else {

                Map<String, String> response = new HashMap<>();
                response.put("error", "Method not allowed");

                sendJsonResponse(exchange,
                        405,
                        mapper.writeValueAsString(response));
            }
        }
    }

    static class StatusHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (exchange.getRequestMethod().equals("GET")) {

                Map<String, String> response = new HashMap<>();
                response.put("status", "UP");
                response.put("database", "OFFLINE");

                sendJsonResponse(exchange,
                        200,
                        mapper.writeValueAsString(response));

            } else {

                Map<String, String> response = new HashMap<>();
                response.put("error", "Method not allowed");

                sendJsonResponse(exchange,
                        405,
                        mapper.writeValueAsString(response));
            }
        }
    }

    private static void sendJsonResponse(
            HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        exchange.getResponseHeaders()
                .add("Content-Type", "application/json");

        exchange.sendResponseHeaders(
                statusCode,
                response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
