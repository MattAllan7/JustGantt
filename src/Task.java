import java.time.LocalDate;
import java.util.ArrayList;

/**
 * An object containing a series of task attributes.
 */
public class Task {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private float progress;
    private ArrayList<Task> dependencies;
    private String resources;
    private String description;

    public Task(String name, LocalDate startDate, int duration) {
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
        endDate = startDate.plusDays(duration);
        progress = 0;
        dependencies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }

}
