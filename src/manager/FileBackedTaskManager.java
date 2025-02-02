package manager;

import tasks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path path;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MM yyyy; HH:mm");

    public FileBackedTaskManager(Path path) {
        super();
        this.path = path;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {

        FileBackedTaskManager manager = new FileBackedTaskManager(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)))) {
            reader.readLine();
            int idCounterMax = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if (!line.isBlank()) {
                    Task task = manager.taskFromString(line);
                    if (task.getType().equals(Type.TASK)) {
                        manager.tasks.put(task.getId(), task);
                    } else if (task.getType().equals(Type.EPIC)) {
                        Epic epic = (Epic) task;
                        manager.epics.put(epic.getId(), epic);
                    } else if (task.getType().equals(Type.SUBTASK)) {
                        SubTask subTask = (SubTask) task;
                        manager.subTasks.put(subTask.getId(), subTask);
                        Epic epic = manager.getEpic(subTask.getEpicId());
                        epic.epicSubTasks.add(subTask.getId());
                        epic.setStatus(manager.getEpicSubTasks(subTask.getEpicId()));
                        epic.calculateTime(manager.getEpicSubTasks(subTask.getEpicId()));
                    }
                    String[] searchMaxCounter = line.split(",");
                    if (Integer.parseInt(searchMaxCounter[0]) > idCounterMax) {
                        idCounterMax = Integer.parseInt(searchMaxCounter[0]);
                    }
                }
            }
            manager.setCounter(idCounterMax + 1);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
        return manager;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(String.valueOf(path))) {
            writer.write("id,type,name,status,description,epic,startTime,endTime,Duration" + "\n");
            getTasks().stream()
                    .forEach(task -> {
                        try {
                            writer.write(anyTaskToString(task) + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка при записи в файл" + path, e);
                        }
                    });

            getEpics().stream()
                    .forEach(epic -> {
                        try {
                            writer.write(anyTaskToString(epic) + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка при записи в файл" + path, e);
                        }
                    });

            getSubTasks().stream()
                    .forEach(subTask -> {
                        try {
                            writer.write(anyTaskToString(subTask) + "\n");
                        } catch (IOException e) {
                            throw new ManagerSaveException("Ошибка при записи в файл" + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл" + path, e);
        }
    }

    public String anyTaskToString(Task task) {
        String line = "";
        if (task.getType().equals(Type.TASK)) {
            line = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + "," + task.getStartTime().format(format)
                    + "," + task.getEndTime().format(format) + "," + task.getDuration().toMinutes();
        } else if (task.getType().equals(Type.EPIC)) {
            if (task.getStartTime() == null || task.getEndTime() == null) {
                line = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription();
            } else {
                line = task.getId() + "," + task.getType() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "," + task.getStartTime().format(format)
                        + "," + task.getEndTime().format(format) + "," + task.getDuration().toMinutes();
            }
        } else if (task.getType().equals(Type.SUBTASK)) {
            SubTask subtask = (SubTask) task;
            line = (subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId() + ","
                    + subtask.getStartTime().format(format) + "," + subtask.getEndTime().format(format) + ","
                    + subtask.getDuration().toMinutes());
        }
        return line;
    }

    public Task taskFromString(String value) {
        String[] preTask = value.split(",");
        Task result = null;
        if (preTask[1].equals("TASK")) {
            Task task = new Task(preTask[2],
                    preTask[4],
                    Status.valueOf(preTask[3]),
                    LocalDateTime.parse(preTask[5], format),
                    Duration.ofMinutes(Long.parseLong(preTask[7])));
            task.setId(Integer.parseInt(preTask[0]));
            result = task;
        } else if (preTask[1].equals("EPIC")) {
            Epic epic = new Epic(preTask[2], preTask[4]);
            epic.setId(Integer.parseInt(preTask[0]));
            epic.calculateTime(getEpicSubTasks(epic.getId()));
            result = epic;
        } else if (preTask[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(preTask[2],
                    preTask[4],
                    Status.valueOf(preTask[3]),
                    LocalDateTime.parse(preTask[6], format),
                    Duration.ofMinutes(Long.parseLong(preTask[8])),
                    Integer.parseInt(preTask[5]));
            subTask.setId(Integer.parseInt(preTask[0]));
            result = subTask;
        }
        return result;
    }

    @Override
    public boolean addNewTask(Task task) {
        boolean flag = super.addNewTask(task);
        save();
        return flag;
    }

    @Override
    public boolean addNewEpic(Epic epic) {
        boolean flag = super.addNewEpic(epic);
        save();
        return flag;
    }

    @Override
    public boolean addNewSubTask(SubTask subTask) {
        boolean flag = super.addNewSubTask(subTask);
            save();
        return flag;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public boolean updateTask(Task task) {
        boolean flag = super.updateTask(task);
        save();
        return flag;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean flag = super.updateEpic(epic);
        save();
        return flag;
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        boolean flag = super.updateSubTask(subTask);
        save();
        return flag;
    }
}

