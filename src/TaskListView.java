import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The middle view.
 * Displays a list of the project's tasks.
 */
public class TaskListView {

    private final ProjectManager projectManager;
    private final Consumer<Task> onEditTask;
    private final Runnable onTasksReordered;

    private HBox taskListBar;
    private VBox taskListPane;
    private ScrollPane taskScrollPane;

    public TaskListView(ProjectManager projectManager, Consumer<Task> onEditTask, Runnable onTasksReordered) {
        this.projectManager = projectManager;
        this.onEditTask = onEditTask;
        this.onTasksReordered = onTasksReordered;

        setupTaskListBar();
        setupTaskListPane();
        refreshUI();
    }

    /**
     * Used to bind to the timeline scrollbar.
     *
     * @return The task list scroll pane.
     */
    public ScrollPane getScrollPane() {
        return taskScrollPane;
    }

    public VBox getView() {
        VBox vBox = new VBox(LayoutValues.NODE_SPACING);
        vBox.setPadding(new Insets(LayoutValues.NODE_SPACING));

        vBox.getChildren().addAll(taskListBar, taskScrollPane);
        VBox.setVgrow(taskScrollPane, Priority.ALWAYS);
        return vBox;
    }

    private void setupTaskListBar() {
        taskListBar = new HBox();
        taskListBar.setAlignment(Pos.CENTER_LEFT);

        taskListBar.setMinHeight(LayoutValues.HEADER_HEIGHT);
        taskListBar.setPrefHeight(LayoutValues.HEADER_HEIGHT);

        Label label = new Label("Tasks");
        label.setFont(LayoutValues.HEADER_FONT);
        label.setPrefHeight(LayoutValues.HEADER_HEIGHT); // Get all formatting from MainView eventually.

        taskListBar.getChildren().add(label);
    }

    private void setupTaskListPane() {
        taskListPane = new VBox(LayoutValues.NODE_SPACING);

        taskScrollPane = new ScrollPane(taskListPane);
        taskScrollPane.setFitToWidth(true);
        taskScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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
        row.setAlignment(Pos.CENTER_LEFT);

        row.setMinHeight(LayoutValues.ROW_HEIGHT);
        row.setPrefHeight(LayoutValues.ROW_HEIGHT);
        row.setMaxHeight(LayoutValues.ROW_HEIGHT);
        return row;

    }

    private Label buildLabel(Task task) {
        Label label = new Label(task.getName());
        label.setFont(LayoutValues.NORMAL_FONT);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMinHeight(LayoutValues.ROW_HEIGHT);
        label.setPrefHeight(LayoutValues.ROW_HEIGHT);
        label.setMaxHeight(LayoutValues.ROW_HEIGHT);
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        addDoubleClickAction(label, task);
        addHighlightAction(label);

        return label;
    }

    private HBox buildButtonBox(int index, int numberOfTasks) {
        Button upButton = new Button("▲");
        Button downButton = new Button("▼");

        upButton.setMinHeight(LayoutValues.ROW_HEIGHT);
        upButton.setPrefHeight(LayoutValues.ROW_HEIGHT);
        upButton.setMaxHeight(LayoutValues.ROW_HEIGHT);
        downButton.setMinHeight(LayoutValues.ROW_HEIGHT);
        downButton.setPrefHeight(LayoutValues.ROW_HEIGHT);
        downButton.setMaxHeight(LayoutValues.ROW_HEIGHT);

        upButton.setDisable(index == 0);
        downButton.setDisable(index == numberOfTasks - 1);

        upButton.setOnAction(_ -> {
            projectManager.moveTaskUp(index);
            onTasksReordered.run();
        });

        downButton.setOnAction(_ -> {
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

        label.setOnMouseEntered(_ -> label.getStyleClass().add("bg-accent-subtle"));

        label.setOnMouseExited(_ -> label.getStyleClass().remove("bg-accent-subtle"));
    }

}