import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutoSaveManager {

    private static final int INTERVAL_SECONDS = 30;

    private final ProjectManager projectManager;
    private final Timeline timeline;

    public AutoSaveManager(ProjectManager projectManager) {
        this.projectManager = projectManager;

        timeline = new Timeline(new KeyFrame(Duration.seconds(INTERVAL_SECONDS),
                _ -> autoSave()));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    /**
     * Saves the project if a file path exists.
     * Silently skips if the project has never been saved (no path set).
     */
    private void autoSave() {
        if(projectManager.hasFilePath()) {
            try {
                projectManager.save();
            } catch(Exception e) {
                System.err.println("Auto-save failed: " + e.getMessage());
            }
        }
    }

}
