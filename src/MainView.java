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
        taskListView = new TaskListView(projectManager, this::refreshAll, ROW_GAP, ROW_HEIGHT);
        timelineView = new TimelineView(projectManager, ROW_GAP, ROW_HEIGHT);
    }

    /**
     * Fills a BorderPane with the four views.
     * Called in JustGanttApp.
     *
     * @return The BorderPane to be displayed in the primaryStage.
     */
    public BorderPane createView() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = menuBarView.getView();
        root.setTop(menuBar);

        BorderPane taskCreatorPane = taskCreatorView.getView();
        root.setLeft(taskCreatorPane);

        BorderPane taskListPane = taskListView.getView();
        root.setCenter(taskListPane);

        BorderPane timelinePane = timelineView.getView();
        root.setRight(timelinePane);

        return root;
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
