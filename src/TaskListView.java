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

    private Label header;
    private VBox taskListPane;

    public TaskListView(ProjectManager projectManager, Runnable onTaskChanged, Consumer<Task> onEditTask, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.onTaskChanged = onTaskChanged;
        this.onEditTask = onEditTask;
        this.rowGap = rowGap;
        this.rowHeight = rowHeight;

        setupHeader();
        setupTaskListPane();
        refreshUI();
    }

    public VBox getView() {
        VBox vBox = new VBox();

        vBox.getChildren().addAll(header, taskListPane);
        return vBox;
    }

    private void setupHeader() {
        header = new Label("Tasks");
        header.setFont(Font.font("System", FontWeight.BOLD, 24));
        header.setPrefHeight(50); // Get all formatting from MainView eventually.
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

            label.setContextMenu(getContextMenu(task));
            addHighlightAction(label);

            taskListPane.getChildren().add(label);
        }
    }

    private ContextMenu getContextMenu(Task task) {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");

        editItem.setOnAction(e -> {
            System.out.println("Edit: " + task.getName());
            onEditTask.accept(task);
        });

        deleteItem.setOnAction(e -> {
            System.out.println("Delete: " + task.getName());
            projectManager.deleteTask(task);
            onTaskChanged.run();
        });

        contextMenu.getItems().addAll(editItem, deleteItem);

        return contextMenu;
    }

    /**
     * Sets events to a label where when the mouse is over the label, change the background color.
     * When the mouse leaves the label's bounds, reset the background color.
     *
     * @param label The label node to add actions to.
     */
    private void addHighlightAction(Label label) {

        label.setOnMouseEntered(e -> {
            label.setStyle("-fx-background-color: lightblue;");
        });

        label.setOnMouseExited(e -> {
            label.setStyle("-fx-background-color: white;"); // Not actually the background color, fix during UI polish.
        });
    }

}
