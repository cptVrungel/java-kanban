package manager;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer server;
    private static final int PORT = 8080;

    public HttpTaskServer(TasksHandler handler) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", handler);
        server.createContext("/epics", handler);
        server.createContext("/subtasks", handler);
        server.createContext("/priority", handler);
        server.createContext("/history", handler);
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer taskServer = new HttpTaskServer(new TasksHandler());
            taskServer.start();  // Запускаем сервер
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен на порту 8080!");
    }

    public void stop() {
        server.stop(1);
        System.out.println("Сервер остановлен!");
    }
}
