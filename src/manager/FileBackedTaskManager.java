package manager;

import tasks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path path;

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
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(anyTaskToString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(anyTaskToString(epic) + "\n");
            }
            for (SubTask subTask : getSubTasks()) {
                writer.write(anyTaskToString(subTask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл" + path, e);
        }
    }

    public String anyTaskToString(Task task) {
        String line = "";
        if (task.getType().equals(Type.TASK)) {
            line = (task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + ",");
        } else if (task.getType().equals(Type.EPIC)) {
            line = (task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + ",");
        } else if (task.getType().equals(Type.SUBTASK)) {
            SubTask subtask = (SubTask) task;
            line = (subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId());
        }
        return line;
    }

    public Task taskFromString(String value) {
        String[] preTask = value.split(",");
        Task result = null;
        if (preTask[1].equals("TASK")) {
            Task task = new Task(preTask[2], preTask[4], Status.valueOf(preTask[3]));
            task.setId(Integer.parseInt(preTask[0]));
            result = task;
        } else if (preTask[1].equals("EPIC")) {
            Epic epic = new Epic(preTask[2], preTask[4]);
            epic.setId(Integer.parseInt(preTask[0]));
            result = epic;
        } else if (preTask[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(preTask[2], preTask[4], Status.valueOf(preTask[3]), Integer.parseInt(preTask[5]));
            subTask.setId(Integer.parseInt(preTask[0]));
            result = subTask;
        }
        return result;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
            save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
            save();
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
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
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
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
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
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}

