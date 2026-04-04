import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entry point for the JustGantt application.
 */
public class JustGanttApp extends Application {

    private Stage primaryStage;
    private ProjectManager projectManager;
    private UserPreferences userPreferences;

    /**
     * Initializes and launches the primary stage of the JustGantt application.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        userPreferences = new UserPreferences();
        applyTheme(userPreferences.getTheme());

        // Default project
        Project project = new Project("Untitled", LocalDate.now());
        projectManager = new ProjectManager(project);

        // Stage
        this.primaryStage = primaryStage;
        updateStageTitle();
        MainView mainView = new MainView(projectManager, setStageSize(primaryStage), this::updateStageTitle, this::applyAndSaveTheme);
        Scene mainScene = new Scene(mainView.createView());
        primaryStage.setScene(mainScene);
        primaryStage.show();

        new AutoSaveManager(projectManager).start();
    }

    /**
     * Applies a theme and saves it to persistent preferences.
     * Passed as a callback into MenuBarView via MainView.
     *
     * @param theme "dark" or "light"
     */
    private void applyAndSaveTheme(String theme) {
        applyTheme(theme);
        userPreferences.setTheme(theme);
    }

    /**
     * Sets the JavaFX user-agent stylesheet to the chosen Cupertino theme.
     * Cupertino theme from AtlantaFX by mkpaz (MIT)
     * <a href="https://github.com/mkpaz/atlantafx">...</a>
     *
     * @param theme "dark" or "light"
     */
    private void applyTheme(String theme) {
        String cssFile = UserPreferences.THEME_DARK.equals(theme) ? "resources/cupertino-dark.css" : "resources/cupertino-light.css";
        URL cssUrl = getClass().getResource(cssFile);
        if(cssUrl == null) {
            System.err.println("Theme file not found: " + cssFile);
            return; // fall back to default JavaFX theme
        }
        Application.setUserAgentStylesheet(cssUrl.toExternalForm());
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

    private void updateStageTitle() {
        primaryStage.setTitle(projectManager.getProjectName() + " - JustGantt");
    }

}
