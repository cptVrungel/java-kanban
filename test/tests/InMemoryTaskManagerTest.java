package tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    protected TaskManager createManager() {
        return Managers.getDefault();
    }

    @Test
    public void TaskHistoryEquals() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);
        Task task = manager.getTask(1);
        Task task0 = manager.getHistory().get(0);
        Assertions.assertNotNull(task);
        Assertions.assertNotNull(task0);
        Assertions.assertEquals(task, task0);
    }

    @Test
    public void HistoryIsEmpty() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);
        int size = manager.getHistory().size();
        Assertions.assertEquals(size, 0);
    }

    @Test
    public void DuplicationHistory() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);
        Task task = manager.getTask(1);
        Task task0 = manager.getTask(2);
        Task task01 = manager.getTask(1);
        int size = manager.getHistory().size();
        Assertions.assertEquals(size, 2);
    }

    @Test
    public void DeleteFromHistory() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);

        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "epic2_description");
        manager.addNewEpic(epic2);

        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 12, 12, 30), Duration.ofMinutes(30), 3);
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 13, 12, 30), Duration.ofMinutes(30), 3);
        manager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.NEW, LocalDateTime.of
                (2025, 04, 14, 12, 30), Duration.ofMinutes(30), 4);
        manager.addNewSubTask(subTask3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubTask(5);
        manager.getSubTask(7);
        manager.deleteSubTask(7);
        int size = manager.getHistory().size();
        Assertions.assertEquals(size, 4);  //удаление с конца

        manager.deleteTask(1);
        size = manager.getHistory().size();
        Assertions.assertEquals(size, 3);  //удаление с начала

        manager.deleteEpic(3);
        size = manager.getHistory().size();
        Assertions.assertEquals(size, 1);  //удаление из середины
    }

    @Test
    public void CleanHistory() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);

        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "epic2_description");
        manager.addNewEpic(epic2);

        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 12, 12, 30), Duration.ofMinutes(30), 3);
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 13, 12, 30), Duration.ofMinutes(30), 3);
        manager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.NEW, LocalDateTime.of
                (2025, 04, 14, 12, 30), Duration.ofMinutes(30), 4);
        manager.addNewSubTask(subTask3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getSubTask(5);
        manager.getSubTask(7);
        manager.cleanHistory();
        int size = manager.getHistory().size();
        Assertions.assertEquals(size, 0);
    }
}