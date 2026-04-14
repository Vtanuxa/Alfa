import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new RootHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Сервер запущен на http://localhost:8080/");

    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("views/index.html");
            String contentType = "html";
            exchange.getResponseHeaders().set("Content-Type", contentType);
            try (OutputStream os = exchange.getResponseBody()) {
                FileInputStream fis = new FileInputStream(file);
                exchange.sendResponseHeaders(200, file.length());
                byte[] buffer = new byte[8192]; // 8KB буфер
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
