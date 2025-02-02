package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String name, String description, Status status, LocalDateTime startTime, Duration duration,
                   int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plus(this.duration);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                ", duration=" + duration +
                ", epicId=" + epicId +
                "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask subTask)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
