import java.time.LocalDate;
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
