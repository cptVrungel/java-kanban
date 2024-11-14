package manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import tasks.*;


public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORYSIZE = 10;
    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task){
        if (task != null) {
            if (history.size() == HISTORYSIZE) {
                history.removeFirst();
            }
            history.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<Task>(history);
    }
}
