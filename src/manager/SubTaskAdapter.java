package manager;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Status;
import tasks.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class SubTaskAdapter extends TypeAdapter<SubTask> {
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
