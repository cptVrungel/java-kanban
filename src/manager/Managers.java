package manager;

import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDrive(Path path) {
        TaskManager manager = FileBackedTaskManager.loadFromFile(path);
        return manager;
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
