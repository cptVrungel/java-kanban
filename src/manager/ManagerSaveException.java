package manager;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable exc) {
        super(message, exc);
    }
}
