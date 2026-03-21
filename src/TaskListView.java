import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

/**
 * The middle view.
 * Displays a list of the project's tasks.
 */
public class TaskListView {

    private ProjectManager projectManager;
    private Runnable onTaskChanged; // Will need for edit and delete.
    private int rowGap;
    private int rowHeight;

    private Label header;
    private VBox taskListPane;

    public TaskListView(ProjectManager projectManager, Runnable onTaskChanged, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.onTaskChanged = onTaskChanged;
        this.rowGap = rowGap;
        this.rowHeight = rowHeight;

        setupHeader();
        setupTaskListPane();
        refreshUI();
    }

    private void setupHeader() {
        header = new Label("Tasks");
        header.setFont(Font.font("System", FontWeight.BOLD, 24));
        header.setPrefHeight(50); // Get all formatting from MainView eventually.
    }

    private void setupTaskListPane() {
        taskListPane = new VBox(rowGap);
    }

    public BorderPane getView() {
        BorderPane borderPane = new BorderPane();

        borderPane.setPrefWidth(400);
        borderPane.setMinWidth(Region.USE_PREF_SIZE);
        borderPane.setMaxWidth(Region.USE_PREF_SIZE);

        borderPane.setTop(header);
        borderPane.setCenter(taskListPane);
        return borderPane;
    }

    public void refreshUI() {
        taskListPane.getChildren().clear();

        ArrayList<Task> tasks = projectManager.getTasks();

        for(Task task : tasks) {
            Label label = new Label(task.getName());
            label.setFont(Font.font("System", FontWeight.NORMAL, 16));
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPrefHeight(rowHeight);
            taskListPane.getChildren().add(label);
        }
    }

}
