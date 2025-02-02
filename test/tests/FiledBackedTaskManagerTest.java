package tests;

import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FiledBackedTaskManagerTest extends TaskManagerTest {

    Path path1 = Paths.get("src/manager/drive.txt");
    Path path2 = Paths.get("src/manager/drive2.txt");


    @Override
    protected TaskManager createManager() {
        try {
            Files.write(path2, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Managers.getDrive(path2);
    }

    @Test
    public void SaveException() {
        FileBackedTaskManager fileManager = (FileBackedTaskManager) manager;
        Assertions.assertDoesNotThrow(() -> fileManager.save());
    }

    @Test
    public void taskFromStringBadInput() {
        FileBackedTaskManager fileManager = (FileBackedTaskManager) manager;
        String input = "1,TASK,Task1,NEW,Description task1,12 04 2000; 13:00"; // нет Duration
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> fileManager.taskFromString(input));
    }
}