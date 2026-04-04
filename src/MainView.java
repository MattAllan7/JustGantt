import javafx.geometry.Rectangle2D;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.util.function.Consumer;

/**
 * Creates one BorderPane from TaskCreatorView, TaskListView, TimelineView, and MenuBarView
 * to be displayed in the primaryStage in JustGanttApp.
 * Defines formatting values in private variables -> ROW_HEIGHT, ROW_GAP.
 */
public class MainView {

    private final Rectangle2D bounds;
    private final Runnable updateStageTitle;

    private final MenuBarView menuBarView;
    private final TaskCreatorView taskCreatorView;
    private final TaskListView taskListView;
    private final TimelineView timelineView;

    /**
     * Constructor which initializes the four views.
     *
     * @param projectManager The ProjectManager, passed to four views.
     */
    public MainView(ProjectManager projectManager, Rectangle2D bounds, Runnable updateStageTitle, Consumer<String> onThemeChanged) {
        this.bounds = bounds;
        this.updateStageTitle = updateStageTitle;
        menuBarView = new MenuBarView(projectManager, this::refreshAll, onThemeChanged);
        taskCreatorView = new TaskCreatorView(projectManager, this::refreshAll);
        taskListView = new TaskListView(projectManager, taskCreatorView::loadTask, this::refreshAll);
        timelineView = new TimelineView(projectManager);
    }

    /**
     * Fills a BorderPane with the four views.
     * Called in JustGanttApp.
     *
     * @return The BorderPane to be displayed in the primaryStage.
     */
    public BorderPane createView() {
        BorderPane borderPane = new BorderPane();
        double boundsWidth = bounds.getWidth();

        MenuBar menuBar = menuBarView.getView();
        borderPane.setTop(menuBar);

        VBox taskCreatorPane = taskCreatorView.getView();
        taskCreatorPane.setMinWidth(0);
        taskCreatorPane.setPrefWidth(boundsWidth/4);
        taskCreatorPane.setMaxWidth(Region.USE_PREF_SIZE);

        VBox taskListPane = taskListView.getView();
        taskListPane.setMinWidth(0);
        taskListPane.setPrefWidth(boundsWidth/4);
        taskListPane.setMaxWidth(boundsWidth/2);

        VBox timelinePane = timelineView.getView();
        timelinePane.setMinWidth(boundsWidth/2);

        taskListView.getScrollPane().vvalueProperty().bindBidirectional(
                timelineView.getScrollPane().vvalueProperty()
        );

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.25, 0.5);
        splitPane.getItems().addAll(taskCreatorPane, taskListPane, timelinePane);

        borderPane.setCenter(splitPane);

        return borderPane;
    }

    /**
     * Calls the refreshUI() method of relevant views.
     */
    public void refreshAll() {
        taskCreatorView.refreshUI();
        taskListView.refreshUI();
        timelineView.refreshUI();
        updateStageTitle.run();
    }

}