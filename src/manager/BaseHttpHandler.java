package manager;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.Status;
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
    protected Gson gson = new GsonBuilder()
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

    public Gson getGson() {
        return gson;
    }

    private static class DateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(formatter));
            } else {
                jsonWriter.nullValue();
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatter);
        }
    }

    private static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            if (duration != null) {
                jsonWriter.value(duration.getSeconds());
            } else {
                jsonWriter.nullValue();
            }
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            long seconds = jsonReader.nextLong();
            return Duration.ofSeconds(seconds);
        }
    }

    private static class TaskAdapter extends TypeAdapter<Task> {
        private final Gson gson;

        public TaskAdapter() {
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
        }

        @Override
        public void write(JsonWriter jsonWriter, Task task) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(task.getName());
            jsonWriter.name("description").value(task.getDescription());
            jsonWriter.name("status").value(task.getStatus().toString());
            jsonWriter.name("id").value(task.getId());
            jsonWriter.name("type").value(task.getType().toString());

            jsonWriter.name("endTime");
            gson.toJson(task.getEndTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("startTime");
            gson.toJson(task.getStartTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("duration");
            gson.toJson(task.getDuration(), Duration.class, jsonWriter);

            jsonWriter.endObject();
        }

        @Override
        public Task read(JsonReader jsonReader) {
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            Status status = Status.valueOf(jsonObject.get("status").getAsString());

            int id;
            if (jsonObject.has("id")) {
                id = jsonObject.get("id").getAsInt();
            } else {
                id = 0;
            }

            LocalDateTime startTime = gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class);
            Duration duration = gson.fromJson(jsonObject.get("duration"), Duration.class);

            Task task = new Task(name, description, status, startTime, duration);
            task.setId(id);

            return task;
        }
    }

    private static class EpicAdapter extends TypeAdapter<Epic> {
        private final Gson gson;

        public EpicAdapter() {
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .serializeNulls()
                    .create();
        }

        @Override
        public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(epic.getName());
            jsonWriter.name("description").value(epic.getDescription());
            jsonWriter.name("status").value(epic.getStatus().toString());
            jsonWriter.name("id").value(epic.getId());
            jsonWriter.name("type").value(epic.getType().toString());
            jsonWriter.name("epicSubTasks").value(epic.epicSubTasks.toString());

            jsonWriter.name("endTime");
            gson.toJson(epic.getEndTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("startTime");
            gson.toJson(epic.getStartTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("duration");
            gson.toJson(epic.getDuration(), Duration.class, jsonWriter);

            jsonWriter.endObject();
        }

        @Override
        public Epic read(JsonReader jsonReader) {
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();

            int id;
            if (jsonObject.has("id")) {
                id = jsonObject.get("id").getAsInt();
            } else {
                id = 0;
            }

            Epic epic = new Epic(name, description);

            return epic;
        }
    }

    private static class SubTaskAdapter extends TypeAdapter<SubTask> {
        private final Gson gson;

        public SubTaskAdapter() {
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .serializeNulls()
                    .create();
        }

        @Override
        public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(subTask.getName());
            jsonWriter.name("description").value(subTask.getDescription());
            jsonWriter.name("status").value(subTask.getStatus().toString());
            jsonWriter.name("id").value(subTask.getId());
            jsonWriter.name("type").value(subTask.getType().toString());
            jsonWriter.name("epicId").value(subTask.getEpicId());

            jsonWriter.name("endTime");
            gson.toJson(subTask.getEndTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("startTime");
            gson.toJson(subTask.getStartTime(), LocalDateTime.class, jsonWriter);

            jsonWriter.name("duration");
            gson.toJson(subTask.getDuration(), Duration.class, jsonWriter);

            jsonWriter.endObject();
        }

        @Override
        public SubTask read(JsonReader jsonReader) {
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            Status status = Status.valueOf(jsonObject.get("status").getAsString());
            Integer epicId = jsonObject.get("epicId").getAsInt();

            Integer id;
            if (jsonObject.has("id")) {
                id = jsonObject.get("id").getAsInt();
            } else {
                id = 0;
            }

            LocalDateTime startTime = gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class);
            Duration duration = gson.fromJson(jsonObject.get("duration"), Duration.class);

            SubTask subTask = new SubTask(name, description, status, startTime, duration, epicId);
            subTask.setId(id);

            return subTask;
        }
    }
}






