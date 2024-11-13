package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;

public interface TaskManager {
    void addNewTask(Task task);

    void deleteTask(int id);

    void deleteTasks();

    Task getTask(int id);

    void updateTask(Task task);

    Collection<Task> getTasks();

    void addNewEpic(Epic epic);

    void deleteEpic(int id);

    void deleteEpics();

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    Collection<Epic> getEpics();

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    void addNewSubTask(SubTask subTask);

    void deleteSubTask(int id);

    void deleteSubTasks();

    SubTask getSubTask(int id);

    void updateSubTask(SubTask subTask);

    Collection<SubTask> getSubTasks();
}