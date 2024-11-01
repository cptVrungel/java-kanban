import java.util.Objects;

public class Task {

    String name;
    String description;
    int id;
    Status status;

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;;
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
