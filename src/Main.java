import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager newManager = Managers.getDrive("src/manager/drive.txt");

        System.out.println(newManager.getTasks());
        System.out.println("----------------------");
        System.out.println(newManager.getEpics());
        System.out.println("----------------------");
        System.out.println(newManager.getSubTasks());

        Task task4 = new Task("task4", "task4_description", Status.NEW);
        Epic epic5 = new Epic("epic5", "epic5_description");
        Epic epic6 = new Epic("epic6", "epic6_description");
        SubTask subTask6 = new SubTask("subTask6", "subTas6_description", Status.IN_PROGRESS, 5);

        newManager.addNewTask(task4);
        newManager.addNewEpic(epic5);
        newManager.addNewEpic(epic6);
        newManager.addNewSubTask(subTask6);

        System.out.println(newManager.getTasks());
        System.out.println("----------------------");
        System.out.println(newManager.getEpics());
        System.out.println("----------------------");
        System.out.println(newManager.getSubTasks());

        /*Task task1 = new Task("task1", "task1_description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1_description");
        Epic epic2 = new Epic("epic2", "epic2_description");
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.IN_PROGRESS, 2);

        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(task1);
        taskManager.addNewTask(epic1);
        taskManager.addNewTask(epic2);
        taskManager.addNewTask(subTask1);

        //TaskManager taskManager = Managers.getDrive("src/manager/drive.txt");
        //taskManager.addNewTask(task1);

        System.out.println(AnyTaskToString(epic1));
    }

        public static String AnyTaskToString(Task task){
            String line = "";
            if (task.getType().equals(Type.TASK)){
                line = (task.getId() + ", " + task.getType()+ ", " + task.getName() + ", " +
                        task.getStatus() + ", " + task.getDescription());
            }
            else if (task.getType().equals(Type.EPIC)){
                line = (task.getId() + ", " + task.getType()+ ", " + task.getName() + ", " +
                        task.getStatus() + ", " + task.getDescription());
            }
            else if (task.getType().equals(Type.SUBTASK)) {
                    SubTask subTask = (SubTask) task;
                    line = (subTask.getId() + ", " + subTask.getType() + ", " + subTask.getName() + ", " +
                            subTask.getStatus() + ", " + subTask.getDescription() + ", " + subTask.getEpicId());
            }
            return line;
        }*/


        /*Task task1 = new Task("task1", "task1_description", Status.NEW);
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

