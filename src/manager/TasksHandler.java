package manager;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static manager.Endpoint.POST_TASK;

public class TasksHandler extends BaseHttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try {
            Endpoint endpoint = getEndpoint(exchange);
            switch (endpoint) {
                case GET_TASKS:
                    get_tasks(exchange);
                    break;
                case GET_TASK:
                    get_task(exchange);
                    break;
                case POST_TASK:
                    post_task(exchange);
                    break;
                case DELETE_TASK:
                    delete_task(exchange);
                    break;
                case GET_EPICS:
                    get_epics(exchange);
                    break;
                case GET_EPIC:
                    get_epic(exchange);
                    break;
                case POST_EPIC:
                    post_epic(exchange);
                    break;
                case DELETE_EPIC:
                    delete_epic(exchange);
                    break;
                case GET_SUBTASKS:
                    get_subTasks(exchange);
                    break;
                case GET_SUBTASK:
                    get_subTask(exchange);
                    break;
                case POST_SUBTASK:
                    post_subTask(exchange);
                    break;
                case DELETE_SUBTASK:
                    delete_subTask(exchange);
                    break;
                case GET_EPIC_SUBTASKS:
                    get_epic_subTasks(exchange);
                    break;
                case PRIORITY:
                    priority(exchange);
                    break;
                case HISTORY:
                    history(exchange);
                    break;
                default:

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void get_tasks(HttpExchange exchange) throws IOException {
        try {
            String response = BaseHttpHandler.gson.toJson(getManager().getTasks());
            sendText(exchange, response, 200);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void get_task(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                Task task = getManager().getTask(id);
                if (task != null) {
                    String response = BaseHttpHandler.gson.toJson(task);
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void post_task(HttpExchange exchange) throws IOException {
        try {
            InputStream input = exchange.getRequestBody();
            String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Task task = BaseHttpHandler.gson.fromJson(body, Task.class);
            boolean flag;
            if (task.getId() == 0) {
                flag = getManager().addNewTask(task);
            } else {
                flag = getManager().updateTask(task);
            }
            if (flag) {
                sendText(exchange, "Задача добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void delete_task(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                Task task = getManager().getTask(id);
                if (task != null) {
                    getManager().deleteTask(id);
                    sendText(exchange, "Задача успешно удалена", 200);
                } else {
                    sendText(exchange, "Такой задачи итак не было в списке", 200);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void get_epics(HttpExchange exchange) throws IOException {
        try {
            String response = BaseHttpHandler.gson.toJson(getManager().getEpics());
            sendText(exchange, response, 200);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void get_epic(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                Epic epic = getManager().getEpic(id);
                if (epic != null) {
                    String response = BaseHttpHandler.gson.toJson(epic);
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void post_epic(HttpExchange exchange) throws IOException {
        try {
            InputStream input = exchange.getRequestBody();
            String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = BaseHttpHandler.gson.fromJson(body, Epic.class);
            epic.setStatus(getManager().getEpicSubTasks(epic.getId()));
            boolean flag = getManager().addNewEpic(epic);
            if (flag) {
                sendText(exchange, "Задача-эпик добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void delete_epic(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                Epic epic = getManager().getEpic(id);
                if (epic != null) {
                    getManager().deleteEpic(id);
                    sendText(exchange, "Задача успешно удалена", 200);
                } else {
                    sendText(exchange, "Такой задачи итак не было в списке", 200);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    private void get_subTasks(HttpExchange exchange) throws IOException {
        try {
            String response = BaseHttpHandler.gson.toJson(getManager().getSubTasks());
            sendText(exchange, response, 200);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void get_subTask(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                SubTask subTask = getManager().getSubTask(id);
                if (subTask != null) {
                    String response = BaseHttpHandler.gson.toJson(subTask);
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void post_subTask(HttpExchange exchange) throws IOException {
        try {
            InputStream input = exchange.getRequestBody();
            String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTask = BaseHttpHandler.gson.fromJson(body, SubTask.class);
            boolean flag;
            if (subTask.getId() == 0) {
                flag = getManager().addNewSubTask(subTask);
            } else {
                flag = getManager().updateSubTask(subTask);
            }
            if (flag) {
                sendText(exchange, "Задача добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void delete_subTask(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                SubTask subTask = getManager().getSubTask(id);
                if (subTask != null) {
                    getManager().deleteSubTask(id);
                    sendText(exchange, "Задача успешно удалена", 200);
                } else {
                    sendText(exchange, "Такой задачи итак не было в списке", 200);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void get_epic_subTasks(HttpExchange exchange) throws IOException {
        try {
            Integer id = getId(exchange);
            if (id != null) {
                Epic epic = getManager().getEpic(id);
                if (epic != null) {
                    String response = BaseHttpHandler.gson.toJson(getManager().getEpicSubTasks(id));
                    sendText(exchange, response, 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void priority(HttpExchange exchange) {
        try {
            String body = BaseHttpHandler.gson.toJson(getManager().getPrioritizedTasks());
            sendText(exchange, body, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void history(HttpExchange exchange) {
        try {
            String body = BaseHttpHandler.gson.toJson(getManager().getHistory());
            sendText(exchange, body, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer getId(HttpExchange exchange) {
        String[] array = exchange.getRequestURI().getPath().split("/");

        if (array.length == 3 | array.length == 4) {
            try {
                return Integer.parseInt(array[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] array = path.split("/");

        if (method.equals("GET") && array[1].equals("tasks") && array.length == 2) {
            return Endpoint.GET_TASKS;
        } else if (method.equals("GET") && array[1].equals("tasks") && array.length == 3) {
            return Endpoint.GET_TASK;
        } else if (method.equals("POST") && array[1].equals("tasks") && array.length == 2) {
            return POST_TASK;
        } else if (method.equals("DELETE") && array[1].equals("tasks") && array.length == 3) {
            return Endpoint.DELETE_TASK;
        } else if (method.equals("GET") && array[1].equals("subtasks") && array.length == 2) {
            return Endpoint.GET_SUBTASKS;
        } else if (method.equals("GET") && array[1].equals("subtasks") && array.length == 3) {
            return Endpoint.GET_SUBTASK;
        } else if (method.equals("POST") && array[1].equals("subtasks") && array.length == 2) {
            return Endpoint.POST_SUBTASK;
        } else if (method.equals("DELETE") && array[1].equals("subtasks") && array.length == 3) {
            return Endpoint.DELETE_SUBTASK;
        } else if (method.equals("GET") && array[1].equals("epics") && array.length == 2) {
            return Endpoint.GET_EPICS;
        } else if (method.equals("GET") && array[1].equals("epics") && array.length == 3) {
            return Endpoint.GET_EPIC;
        } else if (method.equals("GET") && array[1].equals("epics") && array.length == 4) {
            return Endpoint.GET_EPIC_SUBTASKS;
        } else if (method.equals("POST") && array[1].equals("epics") && array.length == 2) {
            return Endpoint.POST_EPIC;
        } else if (method.equals("DELETE") && array[1].equals("epics") && array.length == 3) {
            return Endpoint.DELETE_EPIC;
        } else if (method.equals("GET") && array[1].equals("priority") && array.length == 2) {
            return Endpoint.PRIORITY;
        } else if (method.equals("GET") && array[1].equals("history") && array.length == 2) {
            return Endpoint.HISTORY;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}
