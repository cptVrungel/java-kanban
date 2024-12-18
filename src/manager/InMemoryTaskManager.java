package manager;

import tasks.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    private int counter = 1;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public void addNewTask(Task task) {
        tasks.put(counter, task);
        task.setId(counter);
        counter++;
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public void addNewEpic(Epic epic) {
        epics.put(counter, epic);
        epic.setId(counter);
        counter++;
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (Integer id_Subtask: epic.epicSubTasks){
            subTasks.remove(id_Subtask);
        }
        epics.remove(id);
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())){
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        ArrayList<SubTask> arr = new ArrayList<>();
        for (SubTask subTask: subTasks.values()){
            if (subTask.getEpicId() == epicId){
                arr.add(subTask);
            }
        }
        return arr;
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())){
            subTasks.put(counter, subTask);
            subTask.setId(counter);
            counter++;
            Epic epic = epics.get(subTask.getEpicId());
            epic.epicSubTasks.add(subTask.getId());
            epic.setStatus(getEpicSubTasks(epic.getId()));
        }
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.remove(id);
        epic.epicSubTasks.remove(id);
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic: epics.values()){
            epic.setStatus(getEpicSubTasks(epic.getId()));
            epic.epicSubTasks.clear();
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())){
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.setStatus(getEpicSubTasks(epic.getId()));
        }
    }

    @Override
    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }
}



