public class SubTask extends Task {
        public SubTask(String name, String description, int id, Status status){
            super(name, description, id, status);
        }

    @Override
    public String toString() {
        return "Задача SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
