package tests;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import manager.HttpTaskServer;
import manager.TaskManager;
import manager.TasksHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestServer {

    TasksHandler handler = new TasksHandler();
    HttpTaskServer taskServer = new HttpTaskServer(handler);
    Gson gson;
    TaskManager manager;

    public TestServer() throws IOException {
        this.gson = handler.getGson();
        this.manager = handler.getManager();
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = new ArrayList<>(manager.getTasks());

        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask_priority_history() throws IOException, InterruptedException {

        Task task1 = new Task("Test 1", "Testing task 1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task("Test 2", "Testing task 2", Status.NEW, LocalDateTime.of(2000,
                04, 12, 10, 00), Duration.ofMinutes(5));
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request1 = HttpRequest
                .newBuilder()
                .uri(url1)
                .GET()
                .build();

        URI url2 = URI.create("http://localhost:8080/priority");
        HttpRequest request2 = HttpRequest
                .newBuilder()
                .uri(url2)
                .GET()
                .build();

        URI url3 = URI.create("http://localhost:8080/history");
        HttpRequest request3 = HttpRequest
                .newBuilder()
                .uri(url3)
                .GET()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());

        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(JsonParser.parseString(response3.body()), listType);
        List<Task> priority = gson.fromJson(JsonParser.parseString(response2.body()), listType);
        Task task3 = gson.fromJson(JsonParser.parseString(response1.body()), Task.class);

        Assertions.assertEquals(2, priority.size(), "Некорректное количество задач");
        Assertions.assertEquals(1, history.size(), "Некорректное количество задач");
        Assertions.assertEquals(task1, task3);
    }

    @Test
    public void testAddEpicAndSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("EPIC 1", "hjdkjdfkxfklfxl");
        String epicstr = gson.toJson(epic);
        SubTask subTask1 = new SubTask("Subtask 1", "Testing task 2", Status.NEW, LocalDateTime.of(2000,
                04, 12, 10, 00), Duration.ofMinutes(5), 1);
        String subTask1str = gson.toJson(subTask1);
        SubTask subTask2 = new SubTask("Subtask 2", "Testing task 2", Status.NEW, LocalDateTime.of(2000,
                04, 12, 11, 00), Duration.ofMinutes(5), 1);
        String subTask2str = gson.toJson(subTask2);
        SubTask subTask3 = new SubTask("Subtask 3", "Testing task 2", Status.NEW, LocalDateTime.of(2000,
                04, 12, 10, 00), Duration.ofMinutes(5), 1); // Эту задачу не должно добавить
        String subTask3str = gson.toJson(subTask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url1Epic = URI.create("http://localhost:8080/epics");
        URI urlSubTask = URI.create("http://localhost:8080/subtasks");
        HttpRequest request1 = HttpRequest
                .newBuilder()
                .uri(url1Epic)
                .POST(HttpRequest.BodyPublishers.ofString(epicstr))
                .build();

        HttpRequest request2 = HttpRequest
                .newBuilder()
                .uri(urlSubTask)
                .POST(HttpRequest.BodyPublishers.ofString(subTask1str))
                .build();

        HttpRequest request3 = HttpRequest
                .newBuilder()
                .uri(urlSubTask)
                .POST(HttpRequest.BodyPublishers.ofString(subTask2str))
                .build();

        HttpRequest request4 = HttpRequest
                .newBuilder()
                .uri(urlSubTask)
                .POST(HttpRequest.BodyPublishers.ofString(subTask3str))
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response1.statusCode());
        Assertions.assertEquals(201, response2.statusCode());
        Assertions.assertEquals(201, response3.statusCode());
        Assertions.assertEquals(406, response4.statusCode());

        List<Task> epicsFromManager = new ArrayList<>(manager.getEpics());
        List<Task> subtasksFromManager = new ArrayList<>(manager.getSubTasks());

        Assertions.assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals(2, subtasksFromManager.size(), "Некорректное имя задачи");

        URI urlSubTaskDelete = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request5 = HttpRequest
                .newBuilder()
                .uri(urlSubTaskDelete)
                .DELETE()
                .build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response5.statusCode());
        subtasksFromManager = new ArrayList<>(manager.getSubTasks());
        Assertions.assertEquals(1, subtasksFromManager.size(), "Некорректное имя задачи");

    }
}
