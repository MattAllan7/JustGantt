import java.time.LocalDate;

/**
 * An object containing a series of task attributes.
 */
public class Task {

    private String name;
    private LocalDate startDate;
    private int duration;

    public Task(String name, LocalDate startDate, int duration) {
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
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

    public LocalDate getEndDate() {
        return startDate.plusDays(duration);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
