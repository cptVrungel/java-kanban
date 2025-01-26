import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Path path = Paths.get("src/manager/drive.txt");


        try {
            TaskManager newManager = Managers.getDrive(path);

            System.out.println(newManager.getTasks());
            System.out.println("----------------------");
            System.out.println(newManager.getEpics());
            System.out.println("----------------------");
            System.out.println(newManager.getSubTasks());

            /*Task task2 = new Task("task4", "task4_description", Status.NEW);
            Epic epic2 = new Epic("epic5", "epic5_description");
            Epic epic3 = new Epic("epic6", "epic6_description");
            SubTask subTask2 = new SubTask("subTask6", "subTas6_description", Status.IN_PROGRESS, 5);

            newManager.addNewTask(task2);
            newManager.deleteTask(4);
            newManager.addNewEpic(epic2);
            //newManager.addNewEpic(epic3);
            newManager.addNewSubTask(subTask2);

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(newManager.getTasks());
            System.out.println("----------------------");
            System.out.println(newManager.getEpics());
            System.out.println("----------------------");
            System.out.println(newManager.getSubTasks());*/

            /*Task task3 = new Task("task3", "task3_description", Status.NEW);
            Epic epic2 = new Epic("epic5", "epic5_description");
            Epic epic3 = new Epic("epic6", "epic6_description");
            SubTask subTask2 = new SubTask("subTask6", "subTas6_description", Status.IN_PROGRESS, 5);

            newManager.addNewTask(task3);
            newManager.deleteEpics();*/
            Epic epic10 = new Epic("epic10", "epic10_description");
            SubTask subTask10 = new SubTask("subTask10", "subTas10_description", Status.DONE, 4);
            newManager.addNewEpic(epic10);
            newManager.addNewSubTask(subTask10);
        } catch (ManagerSaveException exc) {
            System.out.println(exc.getMessage());
        }
    }
}

