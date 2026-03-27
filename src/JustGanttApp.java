import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Entry point for the JustGantt application.
 */
public class JustGanttApp extends Application {

    private Stage primaryStage;
    private ProjectManager projectManager;

    /**
     * Initializes and launches the primary stage of the JustGantt application.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Default project:
        Project currentProject = new Project("Untitled", LocalDate.now());
        projectManager = new ProjectManager(currentProject);

        loadSampleData(currentProject);

        this.primaryStage = primaryStage;
        updateStageTitle();
        MainView mainView = new MainView(projectManager, setStageSize(primaryStage), this::updateStageTitle);
        Scene mainScene = new Scene(mainView.createView());
        mainScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Sets a stage's size to that of the user's display.
     * from "perf coder" <a href="https://stackoverflow.com/questions/47054839/stop-resizing-beyond-screen-resolution-range-javafx">...</a>
     *
     * @param stage The stage to set the size of.
     */
    private Rectangle2D setStageSize(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMaxWidth(bounds.getWidth() * 2);
        stage.setMaximized(true);

        return bounds;
    }

    private void loadSampleData(Project project) {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("one", LocalDate.now(), 1));
        tasks.add(new Task("two", LocalDate.now().plusDays(1), 2));
        tasks.add(new Task("three", LocalDate.now().plusDays(2), 3));
        project.addTasks(tasks);
    }

    private void updateStageTitle() {
        primaryStage.setTitle(projectManager.getProjectName() + " - JustGantt");
    }

}
