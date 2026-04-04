import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A list of tasks with a name and a start date.
 * This is saved as a JSON file to the disk.
 */
public class Project {

    private String name;
    private ArrayList<Task> tasks;
    private LocalDate startDate;

    public Project(String name, LocalDate startDate) {
        this.name = name;
        tasks = new ArrayList<>();
        this.startDate = startDate;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}
