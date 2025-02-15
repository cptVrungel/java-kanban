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
            Task task = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of(
                    2025, 04, 11, 16, 30), Duration.ofMinutes(45));

            Epic epic = new Epic("Epic1", "epic1_description");

            SubTask subTask = new SubTask("subTask1", "subTask1_description", Status.DONE, LocalDateTime.of(
                    2025, 04, 12, 16, 30), Duration.ofMinutes(30), 5);

            SubTask subTask2 = new SubTask("subTask1", "subTask1_description", Status.DONE, LocalDateTime.of(
                    2025, 04, 13, 16, 45), Duration.ofMinutes(30), 5);

            SubTask subTask3 = new SubTask("subTask1", "subTask1_description", Status.IN_PROGRESS, LocalDateTime.of(
                    2025, 04, 14, 16, 45), Duration.ofMinutes(30), 5);

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

