package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class Epic extends Task {

    public ArrayList<Integer> epicSubTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
        this.epicSubTasks = new ArrayList<>();
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void calculateTime(ArrayList<SubTask> values) {
        if (values.isEmpty()) {
            this.startTime = null;
            this.endTime = null;
            this.duration = null;
        }
        this.startTime = values.stream()
                .map(subTask -> subTask.getStartTime())
                .min(Comparator.naturalOrder())
                .orElse(null);


        this.endTime = values.stream()
                .map(subTask -> subTask.getEndTime())
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (this.startTime == null || this.endTime == null) {
            this.duration = null;
        } else {
            this.duration = values.stream()
                    .map(subTask -> subTask.getDuration())
                    .reduce(Duration.ZERO, (duration1, duration2) -> duration1.plus(duration2));
        }
    }

    public void setStatus(ArrayList<SubTask> values) {
        if (values == null && epicSubTasks.isEmpty()) {
            status = Status.NEW;
            return;
        }
        if (values.stream().allMatch(subTask -> subTask.getStatus() == Status.NEW)) {
            status = Status.NEW;
        } else if (values.stream().allMatch(subTask -> subTask.getStatus() == Status.DONE)) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", epicSubTasks=" + epicSubTasks +
                "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(epicSubTasks, epic.epicSubTasks) && status == epic.status && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicSubTasks, status, endTime);
    }
}
