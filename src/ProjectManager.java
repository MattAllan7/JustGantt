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

    public void addTask(Task task) {

        // If the entered name is blank or whitespace, do not create a new task.
        if(task.getName().isBlank()) {
            return;
        }

        // If the entered start date comes before the established project start date,
        // update the project start date to the new task's start date.
        LocalDate startDate = task.getStartDate();
        if(startDate.isBefore(project.getStartDate())) {
            project.setStartDate(startDate);
        }

        project.addTask(task);
    }

    public ArrayList<Task> getTasks() {
        return project.getTasks();
    }

    public LocalDate getStartDate() {
        return project.getStartDate();
    }

}
