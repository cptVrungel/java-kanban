public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task("task1", "task1_description", 17, Status.NEW);
        System.out.println(manager.addNewTask(task1));
        Task task2 = new Task("task2", "task2_description", 35, Status.NEW);
        System.out.println(manager.addNewTask(task2));

        Epic epic1 = new Epic("epic1", "epic1_description", 24);
        System.out.println(manager.addNewEpic(epic1));
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", 1, Status.NEW);
        System.out.println(manager.addNewSubTask(epic1, subTask1));
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", 2, Status.IN_PROGRESS);
        System.out.println(manager.addNewSubTask(epic1, subTask2));

        Epic epic2 = new Epic("epic2", "epic2_description", 59);
        System.out.println(manager.addNewEpic(epic2));
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", 1, Status.NEW);
        System.out.println(manager.addNewSubTask(epic2, subTask3));
        manager.printAll();

        Task task = manager.getTask(17);
        task.status = Status.IN_PROGRESS;
        manager.updateTask(task);
        SubTask subTask = manager.getSubTask(epic2, 1);
        subTask.status = Status.DONE;
        manager.updateSubTask(epic2, subTask);
        System.out.println("--------------------------------");
        manager.printAll();
        //Статус у Task № 17 поменялся, у Epic № 59 - рассчитался.

        manager.deleteTask(35);
        manager.deleteEpic(24);
        System.out.println("--------------------------------");
        manager.printAll();

        SubTask subtask4 = new SubTask("subTask4","subTask4_description", 17, Status.IN_PROGRESS);
        Epic epic = manager.getEpic(59);
        manager.addNewSubTask(epic, subtask4);
        epic.description = ("Поменяли описание Epic" + epic.id);
        manager.updateEpic(epic);
        System.out.println("--------------------------------");
        manager.printAll();
        //Статус у Epic № 59 - рассчитался
    }
}
