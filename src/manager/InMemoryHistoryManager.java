package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    
    private Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;

    private int size = 0;

    public int size() {
        return this.size;
    }

    public void removeNode(Node<Task> node) {
        Node<Task> next = node.next;
        Node<Task> prev = node.prev;
        if (prev == null){
            head = node.next;
            if (next != null){
                next.prev = null;
            }
        }
        else if (next == null){
            tail = node.prev;
            prev.next = null;
        }
        else {
            next.prev = prev;
            prev.next = next;
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)){
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
        historyMap.put(task.getId(), newNode);
    }


    @Override
    public List<Task> getHistory() {
        Node <Task> current = head;
        List<Task> history = new ArrayList<>();
        if (current == null){
            return history;
        }
        while (current != null) {
            history.add(current.data);
            current = current.next;
        }
        return history;
    }

    @Override
    public void cleanHistory() {
        for (int key: new HashMap<>(historyMap).keySet()){
            remove(key);
        }
        size = 0;
        head = null;
        tail = null;
    }
}
