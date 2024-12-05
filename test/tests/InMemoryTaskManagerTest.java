package tests;

import manager.InMemoryTaskManager.*;
import static org.junit.jupiter.api.Assertions.*;

import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

class InMemoryTaskManagerTest {

    static TaskManager manager = Managers.getDefault();



    @BeforeAll
    public static void create(){
        Task task1 = new Task("task1", "task1_description", Status.NEW);
        manager.addNewTask(task1);
        Task task2 = new Task("task2", "task2_description", Status.NEW);
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic1", "epic1_description");
        manager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "epic2_description");
        manager.addNewEpic(epic2);
        SubTask subTask1 = new SubTask("subTask1", "subTask1_description", Status.NEW, 3);
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "subTask2_description", Status.NEW, 3);
        manager.addNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "subTask3_description", Status.NEW, 4);
        manager.addNewSubTask(subTask3);
    }

    @BeforeEach
    public void cleanHistory(){
        manager.cleanHistory();
    }

    // Первые шесть тестов - проверка равенство по id (или неравенство, если id разные)
    @Test
    public void TasksShouldBeEqualIfIdIsEqual(){
        Task task1 = manager.getTask(1);
        Task task2 = manager.getTask(1);
        Assertions.assertNotNull(task1);
        Assertions.assertNotNull(task2);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void TasksShouldNotBeEqualIfIdIsEqual(){
        Task task1 = manager.getTask(1);
        Task task2 = manager.getTask(2);
        Assertions.assertNotNull(task1);
        Assertions.assertNotNull(task2);
        Assertions.assertNotEquals(task1, task2);
    }

    @Test
    public void EpicsShouldBeEqualIfIdIsEqual(){
        Task epic1 = manager.getEpic(3);
        Task epic2 = manager.getEpic(3);
        Assertions.assertNotNull(epic1);
        Assertions.assertNotNull(epic2);
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    public void EpicsShouldNotBeEqualIfIdIsEqual(){
        Task epic1 = manager.getEpic(3);
        Task epic2 = manager.getEpic(4);
        Assertions.assertNotNull(epic1);
        Assertions.assertNotNull(epic2);
        Assertions.assertNotEquals(epic1, epic2);
    }

    @Test
    public void SubTasksShouldBeEqualIfIdIsEqual(){
        SubTask subTask1 = manager.getSubTask(5);
        SubTask subTask2 = manager.getSubTask(5);
        Assertions.assertNotNull(subTask1);
        Assertions.assertNotNull(subTask2);
        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test
    public void SubTasksShouldNotBeEqualIfIdIsEqual(){
        SubTask subTask1 = manager.getSubTask(5);
        SubTask subTask2 = manager.getSubTask(6);
        Assertions.assertNotNull(subTask1);
        Assertions.assertNotNull(subTask2);
        Assertions.assertNotEquals(subTask1, subTask2);
    }

    @Test // Тест на сохранность данных задачи после добавления "В историю"
    public void TaskHistoryEquals(){
        Task subTask1 = manager.getSubTask(5);
        Task subTask2 = manager.getHistory().get(0);
        Assertions.assertNotNull(subTask1);
        Assertions.assertNotNull(subTask2);
        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test // Тест на создание объектов-менеджеров
    public void ManagersAreNotNull(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(taskManager);
        Assertions.assertNotNull(historyManager);
    }

    @Test // Тест сохранение данных задачи после добавления в HashMap
    public void TasksAreEqualWhenAddedToMap(){
        Task task3 = new Task("task3", "task3_description", Status.NEW);
        manager.addNewTask(task3);
        Task task4 = manager.getTask(8);
        Assertions.assertNotNull(task3);
        Assertions.assertNotNull(task4);
        Assertions.assertEquals(task3, task4);
    }

    @Test // Тест на корректное определение статуса эпика
    public void CorrectEpicStatus(){
        SubTask subtask1 = manager.getSubTask(5);
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subtask1);
        Assertions.assertEquals(manager.getEpic(3).getStatus(), Status.IN_PROGRESS);
        SubTask subtask2 = manager.getSubTask(6);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.updateSubTask(subtask2);
        Assertions.assertEquals(manager.getEpic(3).getStatus(), Status.DONE);
    }

    @Test // Тест на корректиность размера списка истории при вызове задачи, которая уже вызывалась ранее.
    public void HistorySizeAdd(){
        manager.getTask(1);
        manager.getSubTask(5);
        manager.getEpic(3);
        manager.getTask(1);
        Integer k = manager.getHistory().size();
        Assertions.assertEquals(k, 3);
    }

    @Test // Тест на корректность размера списка истории при удалении задачи
    public void HistorySizeDelete() {
        manager.getTask(1);
        manager.getTask(2);
        manager.getSubTask(6);
        manager.deleteTask(2);
        Assertions.assertEquals(manager.getHistory().size(), 2);
    }
}