import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Entry point for the JustGantt application.
 */
public class JustGanttApp extends Application {

    /**
     * Initializes and launches the primary stage of the JustGantt application.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Default project:
        Project currentProject = new Project("Untitled", LocalDate.now());
        ProjectManager projectManager = new ProjectManager(currentProject);

        loadSampleData(currentProject);

        primaryStage.setTitle(currentProject.getName() + " - JustGantt");
        MainView mainView = new MainView(projectManager);
        primaryStage.setScene(new Scene(mainView.createView(), 1280, 720));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void loadSampleData(Project project) {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("one", LocalDate.now(), 1));
        tasks.add(new Task("two", LocalDate.now().plusDays(1), 2));
        tasks.add(new Task("three", LocalDate.now().plusDays(2), 3));
        project.addTasks(tasks);
    }

}
