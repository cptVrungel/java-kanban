package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> epicSubTasks = new ArrayList<>();
    private Status status = Status.NEW;

    public Epic(String name, String description) {
        super(name, description);
        this.type = Type.EPIC;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(ArrayList<SubTask> values) {
        if (epicSubTasks.isEmpty()) {
            status = Status.NEW;
            return;
        }
        int k = 0;
        int x = 0;
        for (SubTask subTask : values) {
            if (epicSubTasks.contains(subTask.id)) {
                if (subTask.status == Status.IN_PROGRESS) {
                    status = Status.IN_PROGRESS;
                    return;
                } else if (subTask.status == Status.NEW) {
                    k++;
                } else if (subTask.status == Status.DONE) {
                    x++;
                }
            }
        }
        if (k == epicSubTasks.size()) {
            status = Status.NEW;
        } else if (x == epicSubTasks.size()) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                "}";
    }
}
