import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Task task1 = new Task("task1", "task1_description", Status.NEW);
        Task task2 = new Task("task2", "task2_description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1_description");
        Epic epic2 = new Epic("epic2", "epic2_description");
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.IN_PROGRESS, 3);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, 3);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.IN_PROGRESS, 4);

        TaskManager taskManager = Managers.getDefault();

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubTask(subTask1);
        taskManager.addNewSubTask(subTask2);
        taskManager.addNewSubTask(subTask3);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getSubTask(5);
        System.out.println(taskManager.getHistory());

        taskManager.getSubTask(6);
        System.out.println("1-----------------------");
        System.out.println(taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("2-----------------------");
        System.out.println(taskManager.getHistory());
        taskManager.cleanHistory();
        System.out.println("3-----------------------");
        System.out.println(taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println("4-----------------------");
        System.out.println(taskManager.getHistory());


        /*InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("task1", "task1_description", Status.NEW);
        Task task2 = new Task("task2", "task2_description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1_description");
        Epic epic2 = new Epic("epic2", "epic2_description");
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, 3);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, 3);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.NEW, 4);

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.addNewSubTask(subTask3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getEpicSubTasks(3));
        System.out.println(manager.getEpicSubTasks(4));
        System.out.println(manager.getSubTasks());

        Task task = manager.getTask(1);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        SubTask subTask = manager.getSubTask(5);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask);

        System.out.println("--------------------------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getEpicSubTasks(3));
        System.out.println(manager.getEpicSubTasks(4));
        System.out.println(manager.getSubTasks());

        manager.deleteTask(1);
        manager.deleteEpic(3);

        System.out.println("--------------------------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getEpicSubTasks(4));
        System.out.println(manager.getSubTasks());*/
    }
}
