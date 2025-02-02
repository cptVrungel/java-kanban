package tests;

import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {// Менеджер задач, который будет тестироваться

    protected T manager;

    protected abstract T createManager();

    @BeforeEach
    void pickManager() {
        manager  = createManager();
    }

    @Test
    public void epicIsCreatedWhenAddSubTask() {
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 12, 16, 30), Duration.ofMinutes(30), 1);
        Assertions.assertEquals(manager.addNewSubTask(subTask1), false);
        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);
        Assertions.assertEquals(manager.addNewSubTask(subTask1), true);
    }

    @Test // Тест на корректное определение статуса эпика
    public void CorrectEpicStatus() {
        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);

        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.NEW);

        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 12, 16, 30), Duration.ofMinutes(30), 1);
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 13, 16, 45), Duration.ofMinutes(30), 1);
        manager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.NEW, LocalDateTime.of
                (2025, 04, 14, 12, 30), Duration.ofMinutes(30), 1);
        manager.addNewSubTask(subTask3);
        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.NEW);

        SubTask subtask = manager.getSubTask(2);
        Assertions.assertNotNull(subtask);
        subtask.setStatus(Status.DONE);
        manager.updateSubTask(subtask);
        subtask = manager.getSubTask(3);
        Assertions.assertNotNull(subtask);
        subtask.setStatus(Status.DONE);
        manager.updateSubTask(subtask);
        subtask = manager.getSubTask(4);
        Assertions.assertNotNull(subtask);
        subtask.setStatus(Status.DONE);
        manager.updateSubTask(subtask);
        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.DONE);

        subtask.setStatus(Status.NEW);
        manager.updateSubTask(subtask);
        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask);
        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);

        subtask = manager.getSubTask(2);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask);
        subtask = manager.getSubTask(3);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask);
        Assertions.assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void TimeIntervals() {
        Task task1 = new Task("task1", "task1_description", Status.NEW, LocalDateTime.of
                (2025, 04, 10, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW, LocalDateTime.of
                (2025, 04, 11, 12, 30), Duration.ofMinutes(30));
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);
        Assertions.assertEquals(manager.notCrosses(task1, task2), true);

        //проверка на неучет задача с незаданным временем
        Assertions.assertEquals(manager.notCrosses(epic1, task2), true);

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

        //проверка на непересечение эпика с его же подзадачей, у которой самый ранний startTime
        Assertions.assertEquals(manager.notCrosses(epic1, subTask1), true);
        Assertions.assertEquals(manager.notCrosses(epic2, subTask3), true);
    }

}