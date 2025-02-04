package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();

    protected int counter = 1;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected Comparator<Task> comparator = (task1, task2) -> (task1.getStartTime().compareTo(task2.getStartTime()));
    protected final Set<Task> priority = new TreeSet<>(comparator);

    private boolean notCrosses(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null || task1.getEndTime() == null
                || task2.getEndTime() == null) {
            return false;
        }

        boolean flag1 = task1.getEndTime().isAfter(task2.getStartTime());
        boolean flag2 = task1.getStartTime().isBefore(task2.getEndTime());
        return !(flag1 && flag2);
    }

    protected boolean checkCrosses(Task task1) {
        List<Task> list = getPrioritizedTasks();
        if (priority.contains(task1)) {
            list.remove(task1);
        }
        if (!list.isEmpty()) {
            boolean notCrossed = list.stream()
                    .allMatch(streamTask -> notCrosses(task1, streamTask));
            return notCrossed;
        }
        return true;
    }

    protected void setCounter(int count) {
        counter = count;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(priority);
    }

    @Override
    public void cleanHistory() {
        historyManager.cleanHistory();
    }

    @Override
    public int returnCounter() {
        return counter;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean addNewTask(Task task) {
        if (checkCrosses(task)) {
            tasks.put(counter, task);
            task.setId(counter);
            counter++;
            priority.add(task);
            return true;
        }
        return false;
    }

    @Override
    public void deleteTask(int id) {
        Task task = getTask(id);
        tasks.remove(id);
        priority.remove(task);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        tasks.values().stream()
                .forEach(task -> {
                    historyManager.remove(task.getId());
                    priority.remove(task);
                });
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public boolean updateTask(Task task) {
        if (checkCrosses(task)) {
            Task oldTask = getTask(task.getId());
            if (oldTask != null) {
                priority.remove(oldTask);
            }
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
                priority.add(task);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public boolean addNewEpic(Epic epic) {
        epics.put(counter, epic);
        epic.setId(counter);
        counter++;
        return true;
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        epic.epicSubTasks.stream()
                .forEach(x -> {
                    priority.remove(getSubTask(x));
                    historyManager.remove(x);
                    subTasks.remove(x);
                });
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpics() {
        epics.values().stream()
                .forEach(epic -> historyManager.remove(epic.getId()));

        subTasks.values().stream()
                .forEach(subTask -> {
                    historyManager.remove(subTask.getId());
                    priority.remove(subTask);
                });

        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public boolean updateEpic(Epic epic) {
            if (epics.containsKey(epic.getId())) {
                epics.put(epic.getId(), epic);
                return true;
            }
            return false;
    }

    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        List<SubTask> arr = subTasks.values().stream()
                .filter(subTask -> subTask.getEpicId() == epicId)
                .collect(Collectors.toList());
        return new ArrayList<>(arr);
    }

    @Override
    public boolean addNewSubTask(SubTask subTask) {
        if (checkCrosses(subTask)) {
            if (epics.containsKey(subTask.getEpicId())) {
                subTasks.put(counter, subTask);
                subTask.setId(counter);
                counter++;
                Epic epic = epics.get(subTask.getEpicId());
                epic.epicSubTasks.add(subTask.getId());
                epic.setStatus(getEpicSubTasks(epic.getId()));
                epic.calculateTime(getEpicSubTasks(epic.getId()));
                priority.add(subTask);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        subTasks.remove(id);
        epic.epicSubTasks.remove(Integer.valueOf(id));
        epic.setStatus(getEpicSubTasks(epic.getId()));
        epic.calculateTime(getEpicSubTasks(epic.getId()));
        historyManager.remove(id);
        priority.remove(subTask);
    }

    @Override
    public void deleteSubTasks() {
        subTasks.values().stream()
                .forEach(subTask -> {
                    historyManager.remove(subTask.getId());
                    priority.remove(subTask);
                });
        subTasks.clear();
        epics.values().stream()
                .forEach(epic -> {
                    epic.setStatus(getEpicSubTasks(epic.getId()));
                    epic.calculateTime(getEpicSubTasks(epic.getId()));
                    epic.epicSubTasks.clear();
                });
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        if (checkCrosses(subTask)) {
            SubTask oldTask = getSubTask(subTask.getId());
            if (oldTask != null) {
                priority.remove(oldTask);
            }
            if (subTasks.containsKey(subTask.getId())) {
                subTasks.put(subTask.getId(), subTask);
                Epic epic = epics.get(subTask.getEpicId());
                epic.setStatus(getEpicSubTasks(epic.getId()));
                epic.calculateTime(getEpicSubTasks(epic.getId()));
                priority.add(subTask);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }
}



