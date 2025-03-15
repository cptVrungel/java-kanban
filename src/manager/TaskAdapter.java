package manager;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class TaskAdapter extends TypeAdapter<Task> {
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
