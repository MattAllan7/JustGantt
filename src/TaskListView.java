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
    private Consumer<Task> onEditTask;
    private Runnable onTasksReordered;
    private int rowGap;
    private int rowHeight;

    private HBox taskListBar;
    private VBox taskListPane;

    public TaskListView(ProjectManager projectManager, Consumer<Task> onEditTask, Runnable onTasksReordered, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.onEditTask = onEditTask;
        this.onTasksReordered = onTasksReordered;
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

        for(int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            HBox row = buildTaskRow(task, i, tasks.size());
            taskListPane.getChildren().add(row);
        }
    }

    /**
     * Builds a single task row containing up and down reorder buttons and the task name label.
     *
     * @param task The task in the row.
     * @param index The task's current index in the list.
     * @param numberOfTasks The total number of tasks, used to disable buttons at the boundaries.
     * @return The completed HBox row.
     */
    private HBox buildTaskRow(Task task, int index, int numberOfTasks) {

        Label label = buildLabel(task);

        HBox buttonBox = buildButtonBox(index, numberOfTasks);

        HBox row = new HBox(label, buttonBox);
        row.setAlignment(Pos.CENTER_LEFT);                              // MAKE BUTTONS VBOX AND VERTICAL ISNTEAD OF HBOX
        return row;

    }

    private Label buildLabel(Task task) {
        Label label = new Label(task.getName());
        label.setFont(Font.font("System", FontWeight.NORMAL, 16));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPrefHeight(rowHeight);
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        addDoubleClickAction(label, task);
        addHighlightAction(label);

        return label;
    }

    private HBox buildButtonBox(int index, int numberOfTasks) {
        Button upButton = new Button("▲");
        Button downButton = new Button("▼");

        upButton.setDisable(index == 0);
        downButton.setDisable(index == numberOfTasks - 1);

        upButton.setOnAction(e -> {
            projectManager.moveTaskUp(index);
            onTasksReordered.run();
        });

        downButton.setOnAction(e -> {
            projectManager.moveTaskDown(index);
            onTasksReordered.run();
        });

        HBox buttonBox = new HBox(upButton, downButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        return buttonBox;
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
