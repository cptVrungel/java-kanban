package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private void removeNode(Node<Task> node) {
        final Node<Task> next = node.next;
        Node<Task> prev = node.prev;
        if (prev == null) {
            head = node.next;
            if (next != null) {
                next.prev = null;
            }
        } else if (next == null) {
            tail = node.prev;
            prev.next = null;
        } else {
            next.prev = prev;
            prev.next = next;
        }
    }

    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;

    private int size = 0;

    public int size() {
        return this.size;
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
            size--;
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
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
    }

    @Override
    public void cleanHistory() {
        historyMap.clear();
        size = 0;
        head = null;
        tail = null;
    }


    @Override
    public List<Task> getHistory() {
        Node<Task> current = head;
        List<Task> history = new ArrayList<>();
        if (current == null) {
            return history;
        }
        while (current != null) {
            history.add(current.data);
            current = current.next;
        }
        return history;
    }

    private static class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
