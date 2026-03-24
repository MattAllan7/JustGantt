import javafx.scene.layout.*;
import javafx.scene.control.*;

/**
 * Creates one BorderPane from TaskCreatorView, TaskListView, TimelineView, and MenuBarView
 * to be displayed in the primaryStage in JustGanttApp.
 * Defines formatting values in private variables -> ROW_HEIGHT, ROW_GAP.
 */
public class MainView {

    private final int ROW_HEIGHT = 25;
    private final int ROW_GAP = 5;

    private MenuBarView menuBarView;
    private TaskCreatorView taskCreatorView;
    private TaskListView taskListView;
    private TimelineView timelineView;

    /**
     * Constructor which initializes the four views.
     *
     * @param projectManager The ProjectManager, passed to four views.
     */
    public MainView(ProjectManager projectManager) {
        menuBarView = new MenuBarView();
        taskCreatorView = new TaskCreatorView(projectManager, this::refreshAll, ROW_GAP);
        taskListView = new TaskListView(projectManager, this::refreshAll, taskCreatorView::loadTask, ROW_GAP, ROW_HEIGHT);
        timelineView = new TimelineView(projectManager, ROW_GAP, ROW_HEIGHT);
    }

    /**
     * Fills a BorderPane with the four views.
     * Called in JustGanttApp.
     *
     * @return The BorderPane to be displayed in the primaryStage.
     */
    public BorderPane createView() {
        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = menuBarView.getView();
        borderPane.setTop(menuBar);

        VBox taskCreatorPane = taskCreatorView.getView();
        taskCreatorPane.setMinWidth(0);
//        taskCreatorPane.setPrefWidth(300);
//        taskCreatorPane.setMaxWidth(Region.USE_PREF_SIZE);

        VBox taskListPane = taskListView.getView();
//        taskListPane.setMinWidth(Region.USE_PREF_SIZE);
//        taskListPane.setPrefWidth(250);
//        taskListPane.setMaxWidth(500);

        ScrollPane timelinePane = timelineView.getView();

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(taskCreatorPane, taskListPane, timelinePane);
        splitPane.setDividerPositions(0.25f, 0.5f);
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

    }

}
