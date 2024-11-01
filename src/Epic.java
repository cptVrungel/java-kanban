import java.util.HashMap;

public class Epic extends Task {

    private Status status = Status.NEW; //Нельзя изменить статус Epic вручную

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    HashMap<Integer, SubTask> epicSubTasks = new HashMap<>();

    public void setStatus(){
        if (epicSubTasks.isEmpty()){
            status = Status.NEW;
            return;
        }
        int k_new = 0;
        int k_done = 0;
        for (SubTask subtask: epicSubTasks.values()){
            if (subtask.status == Status.IN_PROGRESS){
                status = Status.IN_PROGRESS;
                return;
            }
            else if(subtask.status == Status.NEW){
                k_new += 1;
            }
            else if(subtask.status == Status.DONE){
                k_done += 1;
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
        return "Задача Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';

    }
}
