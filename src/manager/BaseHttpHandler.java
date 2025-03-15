package manager;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseHttpHandler implements HttpHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy; HH:mm");
    protected TaskManager manager = Managers.getDefault();
    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .serializeNulls()
            .create();

    protected void sendText(HttpExchange exchange, String text, Integer responseCode) throws IOException {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.getResponseBody();
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write("Такой задачи не существует".getBytes());
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write("Задача пересекается по времени с существующими".getBytes());
        exchange.close();
    }

    public TaskManager getManager() {
        return manager;
    }
}






