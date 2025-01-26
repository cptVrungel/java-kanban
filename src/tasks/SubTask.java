package tasks;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", epicId=" + epicId +
                "}";
    }
}
