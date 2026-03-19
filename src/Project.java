import java.time.LocalDate;
import java.util.ArrayList;

public class Project {

    private String name;
    private ArrayList<Task> tasks;
    private LocalDate startDate;

    public Project() {
        tasks = new ArrayList<>();
    }

    // temporary.
    public Project(String name, ArrayList<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public Project(String name, ArrayList<Task> tasks, LocalDate startDate) {
        this.name = name;
        this.tasks = tasks;
        this.startDate = startDate;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

}
