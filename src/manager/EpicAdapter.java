package manager;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class EpicAdapter extends TypeAdapter<Epic> {
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
