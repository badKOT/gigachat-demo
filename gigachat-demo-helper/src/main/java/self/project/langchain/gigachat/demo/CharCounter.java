package self.project.langchain.gigachat.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

public class CharCounter {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/count", exchange -> {
            System.out.println("Got request on /count");
            InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
            ObjectMapper om = new ObjectMapper();
            CountRequest rqBody = om.readValue(isr, CountRequest.class);
            
            var result = count(rqBody.text(), rqBody.symbol());
            var response = om.writeValueAsString(result);
            System.out.println("Получил запрос с параметрами " + rqBody + ", результат: " + result);
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.createContext("/hello", exchange -> {
            System.out.println("Got request on /hello");
            String response = "Hi, I got your request! Can I help you?";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.createContext("/shutdown", exchange -> {
            System.out.println("Got request on /shutdown");
            String response = "Got it, shutting down in 3... 2... 1...";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            server.stop(3);
        });
        server.start();
    }

    static CountResult count(String text, String symbol) {
        if (text == null || text.isEmpty() || symbol == null) {
            return new CountResult(0L);
        }
        if (!stringIsAlphanumeric(symbol)) {
            throw new RuntimeException("Я еще учусь и пока умею искать только цифры и буквы. В аргументах передано " + symbol);
        }
        
        return new CountResult(Pattern.compile(symbol.toString()).matcher(text).results().count());
    }

    static boolean stringIsAlphanumeric(String str) {
        return str.matches("[a-zA-Zа-яА-ЯйЙ0-9]+");
    }

    static record CountRequest(String text, String symbol) {}
    static record CountResult(Long count) {}
}
