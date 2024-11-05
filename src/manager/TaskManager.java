package manager;

import tasks.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int counter = 1;

    public void addNewTask(Task task) {
        tasks.put(counter, task);
        task.setId(counter);
        counter++;
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public void addNewEpic(Epic epic) {
        epics.put(counter, epic);
        epic.setId(counter);
        counter++;
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (Integer id_Subtask: epic.epicSubTasks){
            subTasks.remove(id_Subtask);
        }
        epics.remove(id);
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())){
            epics.put(epic.getId(), epic);
        }
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        ArrayList<SubTask> arr = new ArrayList<>();
        for (SubTask subTask: subTasks.values()){
            if (subTask.getEpicId() == epicId){
                arr.add(subTask);
            }
        }
        return arr;
    }

    public void addNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())){
            subTasks.put(counter, subTask);
            subTask.setId(counter);
            counter++;
            Epic epic = epics.get(subTask.getEpicId());
            epic.epicSubTasks.add(subTask.getId());
            epic.setStatus(subTasks.values());
        }
    }

    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.remove(id);
        epic.setStatus(subTasks.values());
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic: epics.values()){
            epic.setStatus(subTasks.values());
            epic.epicSubTasks.clear();
        }
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())){
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.setStatus(subTasks.values());
        }
    }

    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }
}
