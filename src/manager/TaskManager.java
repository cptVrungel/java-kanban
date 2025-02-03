package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    int returnCounter();

    //boolean checkCrosses(Task task1);

    void cleanHistory();

    //boolean notCrosses(Task task1, Task task2);

    Set<Task> getPrioritizedTasks();

    boolean addNewTask(Task task);

    List<Task> getHistory();

    void deleteTask(int id);

    void deleteTasks();

    Task getTask(int id);

    boolean updateTask(Task task);

    Collection<Task> getTasks();

    boolean addNewEpic(Epic epic);

    void deleteEpic(int id);

    void deleteEpics();

    Epic getEpic(int id);

    boolean updateEpic(Epic epic);

    Collection<Epic> getEpics();

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    boolean addNewSubTask(SubTask subTask);

    void deleteSubTask(int id);

    void deleteSubTasks();

    SubTask getSubTask(int id);

    boolean updateSubTask(SubTask subTask);

    Collection<SubTask> getSubTasks();

    //void setCounter(int count);
}
