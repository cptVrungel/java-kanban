import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();



    public String addNewTask(Task task) {
        tasks.put(task.id, task);
        return ("Задача " + task.name + " сохранена под идентификатором " + task.id);
    }

    public String addNewEpic(Epic epic) {
        epics.put(epic.id, epic);
        return ("Задача " + epic.name + " сохранена под идентификатором " + epic.id);
    }

    public String addNewSubTask(Epic epic, SubTask subTask) {
        epic.epicSubTasks.put(subTask.id, subTask);
        epic.setStatus();
        return ("Подзадача subtask " + subTask.name + " сохранена под идентификатором " + subTask.id +
                " в задаче epic " + epic.name + " с идентификатором " + epic.id);

    }

    public void printAll() {
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                System.out.println(task);
            }
        }
        else {
            System.out.println("Нет задач типа Task");
        }

        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                System.out.print(epic);
                System.out.println(", включает следующие подзадачи SubTask:");
                for (SubTask subtask: epic.epicSubTasks.values()) {
                    System.out.println(subtask);
                }
            }
        }
        else {
            System.out.println("Нет задач типа Epic");
        }
    }

    public void deleteAll(){
        tasks.clear();
        epics.clear();
        System.out.println("Список задач очищен");
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    public void deleteSubTask(Epic epic, int id) {
        epic.epicSubTasks.remove(id);
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public SubTask getSubTask(Epic epic, int id){
        return epic.epicSubTasks.get(id);
    }

    public void updateTask(Task task) {
        for (Task task0: tasks.values()) {
            if (task.id == task0.id) {
                tasks.put(task.id, task);
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (Epic epic0: epics.values()) {
            if (epic.id == epic0.id) {
                epics.put(epic.id, epic);
            }
        }
        epic.setStatus();
    }

    public void updateSubTask(Epic epic, SubTask subTask) {
        for (SubTask subTask0: epic.epicSubTasks.values()) {
            if (subTask.id == subTask0.id) {
                epic.epicSubTasks.put(subTask.id, subTask);
            }
        }
        epic.setStatus();
    }

    public void printEpicSubTasks(Epic epic){
        if (!epic.epicSubTasks.isEmpty()) {
                System.out.println("Задача " + epic.name + " включает следующие подзадачи SubTask:");
                for (SubTask subtask: epic.epicSubTasks.values()) {
                    System.out.println(subtask);
                }
        }
        else {
            System.out.println("Нет подзадач");
        }
    }
}