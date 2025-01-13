package manager;

import tasks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileBackedTaskManager extends InMemoryTaskManager {

    boolean isLoading = true;

    static FileBackedTaskManager loadFromFile(String pathString) {

        FileBackedTaskManager manager = new FileBackedTaskManager();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/manager/drive.txt"))) {
            reader.readLine();   //пропуск первой строки с шапкой
            while (reader.ready()) {
                String line = reader.readLine();
                Task task = manager.taskFromString(line);
                if (task.getType().equals(Type.TASK)) {
                    manager.addNewTask(task);
                } else if (task.getType().equals(Type.EPIC)) {
                    Epic epic = (Epic) task;
                    manager.addNewEpic(epic);
                } else if (task.getType().equals(Type.SUBTASK)) {
                    SubTask subTask = (SubTask) task;
                    manager.addNewSubTask(subTask);
                }
            }
            manager.endLoading(); //далее - при записи задач методы add... будут реализовывать метод save()
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return manager;
    }

    public void endLoading() {
        isLoading = false;
    }

    public void save() {
        try (FileWriter writer = new FileWriter("src/manager/drive.txt")) {
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
            System.out.println("Произошла ошибка во время записи в файл.");
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
            result = new Task(preTask[2], preTask[4], Status.valueOf(preTask[3]));
        } else if (preTask[1].equals("EPIC")) {
            Epic epic = new Epic(preTask[2], preTask[4]);
            result = epic;
        } else if (preTask[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(preTask[2], preTask[4], Status.valueOf(preTask[3]), Integer.parseInt(preTask[5]));
            result = subTask;
        }
        return result;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        if (!isLoading) {
            save();
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        if (!isLoading) {
            save();
        }
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        if (!isLoading) {
            save();
        }
    }
}

