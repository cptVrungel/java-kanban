package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.Type;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();

    protected int counter = 1;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected Comparator<Task> comparator = (task1, task2) -> {
        if (task1 instanceof Epic && task2 instanceof SubTask) {
            return -1;
        }
        if (task1 instanceof SubTask && task2 instanceof Epic) {
            return 1; // SubTask идет после Epic
        }

        int timeComparison = task1.getStartTime().compareTo(task2.getStartTime());
        if (timeComparison != 0) {
            return timeComparison;
        }

        return Integer.compare(task1.getId(), task2.getId());

    };

    @Override
    public Set<Task> getPrioritizedTasks() {
        TreeSet<Task> result = Stream.of(tasks, epics, subTasks)
                .flatMap(map -> map.values().stream())
                .filter(task -> {
                    LocalDateTime b = task.getStartTime();
                    return b != null;
                })
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
        return result;
    }

    @Override
    public boolean notCrosses(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null || task1.getEndTime() == null
                || task2.getEndTime() == null) {
            return true;
        }
        if (task1.getType() == Type.EPIC && task2.getType() == Type.SUBTASK) {
            ArrayList<SubTask> epicSubTasks = getEpicSubTasks(task1.getId());
            if (epicSubTasks.contains(task2)) {
                return true;
            }
        } else if (task1.getType() == Type.SUBTASK && task2.getType() == Type.EPIC) {
            ArrayList<SubTask> epicSubTasks = getEpicSubTasks(task2.getId());
            if (epicSubTasks.contains(task1)) {
                return true;
            }
        }
        boolean flag1 = task1.getEndTime().isAfter(task2.getStartTime());
        boolean flag2 = task1.getStartTime().isBefore(task2.getEndTime());
        return !(flag1 && flag2);
    }

    @Override
    public boolean checkCrossesForAdd(Task task1) {
        if (!getPrioritizedTasks().isEmpty()) {
            boolean notCrossed = getPrioritizedTasks().stream()
                    .allMatch(streamTask -> notCrosses(task1, streamTask));
            return notCrossed;
        }
        return true;
    }

    @Override
    public boolean checkCrossesForUpdate(Task task1) {
        Set<Task> newSet = getPrioritizedTasks();
        newSet.remove(task1);
        if (!newSet.isEmpty()) {
            boolean notCrossed = newSet.stream()
                    .allMatch(streamTask -> notCrosses(task1, streamTask));
            return notCrossed;
        }
        return true;
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
    public void setCounter(int count) {
        counter = count;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean addNewTask(Task task) {
        if (checkCrossesForAdd(task)) {
            tasks.put(counter, task);
            task.setId(counter);
            counter++;
            return true;
        }
        return false;
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        tasks.values().stream()
                .forEach(task -> historyManager.remove(task.getId()));
    }


    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public boolean updateTask(Task task) {
        if (checkCrossesForUpdate(task)) {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
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
        epic.calculateTime(getEpicSubTasks(epic.getId()));
        if (checkCrossesForAdd(epic)) {
            epics.put(counter, epic);
            epic.setId(counter);
            counter++;
            return true;
        }
        return false;
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        epic.epicSubTasks.stream()
                .forEach(x -> subTasks.remove(x));
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpics() {
        epics.values().stream()
                .forEach(epic -> historyManager.remove(epic.getId()));

        subTasks.values().stream()
                .forEach(subTask -> historyManager.remove(subTask.getId()));

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
        if (checkCrossesForUpdate(epic)) {
            if (epics.containsKey(epic.getId())) {
                epics.put(epic.getId(), epic);
                return true;
            }
            return false;
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
        if (checkCrossesForAdd(subTask)) {
            if (epics.containsKey(subTask.getEpicId())) {
                subTasks.put(counter, subTask);
                subTask.setId(counter);
                counter++;
                Epic epic = epics.get(subTask.getEpicId());
                epic.epicSubTasks.add(subTask.getId());
                epic.setStatus(getEpicSubTasks(epic.getId()));
                epic.calculateTime(getEpicSubTasks(epic.getId()));
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
    }

    @Override
    public void deleteSubTasks() {
        subTasks.values().stream()
                .forEach(subTask -> historyManager.remove(subTask.getId()));
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
        if (checkCrossesForUpdate(subTask)) {
            if (subTasks.containsKey(subTask.getId())) {
                subTasks.put(subTask.getId(), subTask);
                Epic epic = epics.get(subTask.getEpicId());
                epic.setStatus(getEpicSubTasks(epic.getId()));
                epic.calculateTime(getEpicSubTasks(epic.getId()));
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



