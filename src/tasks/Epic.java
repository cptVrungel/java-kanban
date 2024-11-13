package tasks;

import java.util.ArrayList;
import java.util.Collection;


public class Epic extends Task {

    private Status status = Status.NEW;
    public ArrayList<Integer> epicSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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
        int k_new = 0;
        int k_done = 0;
        for (SubTask subTask: values) {
            if (epicSubTasks.contains(subTask.id)) {
                if (subTask.status == Status.IN_PROGRESS) {
                    status = Status.IN_PROGRESS;
                    return;
                } else if (subTask.status == Status.NEW) {
                    k_new++;
                } else if (subTask.status == Status.DONE) {
                    k_done++;
                }
            }
        }
        if (k_new == epicSubTasks.size()){
            status = Status.NEW;
        }
        else if (k_done == epicSubTasks.size()){
            status = Status.DONE;
        }
        else {
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
                "} ";
    }
}
