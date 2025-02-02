import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        try {
            Task task = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                    (2025, 04, 15, 15, 30), Duration.ofMinutes(45));

            //TaskManager x = Managers.getDefault();

            //System.out.println(x.addNewTask(task));

            Epic epic = new Epic("Epic1", "epic1_description");

            //System.out.println(x.addNewEpic(epic));

            SubTask subTask = new SubTask("subTask1", "subTask1_description", Status.DONE, LocalDateTime.of
                    (2025, 04, 12, 16, 30), Duration.ofMinutes(30), 5);

            SubTask subTask2 = new SubTask("subTask1", "subTask1_description", Status.DONE, LocalDateTime.of
                    (2025, 04, 13, 16, 45), Duration.ofMinutes(30), 5);

            SubTask subTask3 = new SubTask("subTask1", "subTask1_description", Status.IN_PROGRESS, LocalDateTime.of
                    (2025, 04, 14, 12, 30), Duration.ofMinutes(30), 5);

        /*System.out.println(x.addNewSubTask(subTask));
        //System.out.println(x.addNewSubTask(subTask2));
        //System.out.println(x.addNewSubTask(subTask3));

        System.out.println(epic.getEndTime());
        System.out.println(epic.getDuration());

        System.out.println("----------------------");
        System.out.println(x.getPrioritizedTasks());
        System.out.println("----------------------");
        System.out.println(x.Crosses(subTask, subTask2));
        System.out.println("----------------------");*/

            Path path = Paths.get("src/manager/drive.txt");

            TaskManager x = Managers.getDrive(path);

            System.out.println(x.addNewTask(task));
            System.out.println(x.addNewEpic(epic));
            System.out.println(x.addNewSubTask(subTask));
            System.out.println(x.addNewSubTask(subTask2));
            System.out.println(x.addNewSubTask(subTask3));

        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }
}

