import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The middle view.
 * Displays a list of the project's tasks.
 */
public class TaskListView {

    private ProjectManager projectManager;
    private Runnable onTaskChanged; // Will need for edit and delete.
    private Consumer<Task> onEditTask;
    private int rowGap;
    private int rowHeight;

    private HBox taskListBar;
    private VBox taskListPane;

    public TaskListView(ProjectManager projectManager, Runnable onTaskChanged, Consumer<Task> onEditTask, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.onTaskChanged = onTaskChanged;
        this.onEditTask = onEditTask;
        this.rowGap = rowGap;
        this.rowHeight = rowHeight;

        setupTaskListBar();
        setupTaskListPane();
        refreshUI();
    }

    public VBox getView() {
        VBox vBox = new VBox();

        vBox.getChildren().addAll(taskListBar, taskListPane);
        return vBox;
    }

    private void setupTaskListBar() {
        taskListBar = new HBox();
        taskListBar.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Tasks");
        label.setFont(Font.font("System", FontWeight.BOLD, 24));
        label.setPrefHeight(50); // Get all formatting from MainView eventually.

        taskListBar.getChildren().add(label);
    }

    private void setupTaskListPane() {
        taskListPane = new VBox(rowGap);
    }

    public void refreshUI() {
        taskListPane.getChildren().clear();

        ArrayList<Task> tasks = projectManager.getTasks();

        for(Task task : tasks) {
            Label label = new Label(task.getName());
            label.setFont(Font.font("System", FontWeight.NORMAL, 16));
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPrefHeight(rowHeight);

            addDoubleClickAction(label, task);
            addHighlightAction(label);

            taskListPane.getChildren().add(label);
        }
    }

    /**
     * Adds a double click action to a task label.
     * When a label is double-clicked, the TaskCreatorView updates to edit the clicked task.
     * From: <a href="https://www.youtube.com/watch?v=F7-gdu5ru3o">...</a>
     *
     * @param label The label node to add actions to.
     */
    private void addDoubleClickAction(Label label, Task task) {
        label.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2) {
                System.out.println("Double");
                onEditTask.accept(task);
            }
        });
    }

    /**
     * Adds events to a task label where when the mouse is over the label, change the background color.
     * When the mouse leaves the label's bounds, reset the background color.
     *
     * @param label The label node to add actions to.
     */
    private void addHighlightAction(Label label) {

        label.setOnMouseEntered(e -> {
            label.setStyle("-fx-background-color: -fx-selection-bar;");
        });

        label.setOnMouseExited(e -> {
            label.setStyle("");
        });
    }

}
