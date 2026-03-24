import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Performs Project operations.
 * Classes interact with a ProjectManager instead of the Project directly.
 */
public class ProjectManager {

    private Project project;

    public ProjectManager(Project project) {
        this.project = project;
    }

    public void addTask(String name, LocalDate startDate, int duration) {

        validateTaskInputs(name, startDate);

        Task task = new Task(name, startDate, duration);

        project.addTask(task);
    }

    public void updateTask(Task task, String name, LocalDate startDate, int duration) {

        validateTaskInputs(name, startDate);

        task.setName(name);
        task.setStartDate(startDate);
        task.setDuration(duration);
    }

    public boolean deleteTask(Task task) {
        return project.getTasks().remove(task);
    }

    private void updateStartDate(LocalDate startDate) {
        project.setStartDate(startDate);
    }

    public ArrayList<Task> getTasks() {
        return project.getTasks();
    }

    public LocalDate getStartDate() {
        return project.getStartDate();
    }

    /**
     * Finds and returns the number of days the project is.
     * This is the number of days between the project start date and the latest task's end date.
     *
     * @return An int representing the difference between the latest task's end date and the project's start date.
     */
    public int getProjectLength() {
        int projectLength = 0;
        LocalDate projectStartDate = getStartDate();
        for(Task task : getTasks()) {
            int daysBetween = (int) ChronoUnit.DAYS.between(projectStartDate, task.getEndDate());
            if(daysBetween > projectLength)
                projectLength = daysBetween;
        }
        return projectLength;
    }

    private void validateTaskInputs(String name, LocalDate startDate) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }

        if(startDate == null) {
            throw new IllegalArgumentException("Task must have a start date");
        }

        if(startDate.isBefore(project.getStartDate())) {
            throw new IllegalArgumentException("Task cannot start before the project");
        }

    }

}
