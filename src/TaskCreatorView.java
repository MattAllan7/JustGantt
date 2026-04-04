import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The left view.
 * Allows the user to enter various fields to create a new Task.
 */
public class TaskCreatorView {

    private final ProjectManager projectManager;
    private final Runnable onTaskChanged;

    private Task editingTask = null;
    private Button addSaveButton;
    private Button cancelButton;
    private Button deleteButton;
    private final String CREATE_HEADER = "Create New Task";

    private Label header;
    private VBox taskCreatorPane;

    private TextField nameField;
    private DatePicker startDatePicker;
    private Spinner<Integer> durationSpinner;

    public TaskCreatorView(ProjectManager projectManager, Runnable onTaskChanged) {
        this.projectManager = projectManager;
        this.onTaskChanged = onTaskChanged;

        setupHeader();
        setupTaskCreatorPane();
    }

    public VBox getView() {
        VBox vBox = new VBox(LayoutValues.NODE_SPACING);
        vBox.setPadding(new Insets(LayoutValues.NODE_SPACING));

        vBox.getChildren().addAll(header, taskCreatorPane);
        return vBox;
    }

    private void setupHeader() {
        header = new Label(CREATE_HEADER);
        header.setFont(LayoutValues.HEADER_FONT);
        header.setPrefHeight(LayoutValues.HEADER_HEIGHT); // Get all formatting from MainView eventually.
    }

    private void setupTaskCreatorPane() {
        taskCreatorPane = new VBox(LayoutValues.NODE_SPACING);

        setupNameRow();
        setupStartDateRow();
        setupDurationRow();
        setupButtonRow();
    }

    private void setupNameRow() {
        HBox nameRow = new HBox();

        Label nameLabel = new Label("Name:");
        nameField = new TextField("Task " + (projectManager.getTasks().size()+1));
        nameField.setMinWidth(LayoutValues.FIELD_WIDTH);
        nameField.setPrefWidth(LayoutValues.FIELD_WIDTH);
        nameField.setMaxWidth(LayoutValues.FIELD_WIDTH);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        nameRow.getChildren().addAll(nameLabel, spacer, nameField);
        nameRow.setAlignment(Pos.CENTER);
        taskCreatorPane.getChildren().add(nameRow);
    }

    private void setupStartDateRow() {
        HBox startDateRow = new HBox();

        Label startDateLabel = new Label("Start Date:");
        startDatePicker = new DatePicker(projectManager.getStartDate());
        startDatePicker.setMinWidth(LayoutValues.FIELD_WIDTH);
        startDatePicker.setPrefWidth(LayoutValues.FIELD_WIDTH);
        startDatePicker.setMaxWidth(LayoutValues.FIELD_WIDTH);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        startDateRow.getChildren().addAll(startDateLabel, spacer, startDatePicker);
        startDateRow.setAlignment(Pos.CENTER);
        taskCreatorPane.getChildren().add(startDateRow);
    }

    private void setupDurationRow() {
        HBox durationRow = new HBox();
        Label durationLabel = new Label("Duration:");

        int minValue = 1;
        int maxValue = 1000;
        int initialValue = 1;
        durationSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue);
        durationSpinner.setValueFactory(valueFactory);

        durationSpinner.setEditable(true);
        durationSpinner.setMinWidth(LayoutValues.FIELD_WIDTH);
        durationSpinner.setPrefWidth(LayoutValues.FIELD_WIDTH);
        durationSpinner.setMaxWidth(LayoutValues.FIELD_WIDTH);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        durationRow.getChildren().addAll(durationLabel, spacer, durationSpinner);
        durationRow.setAlignment(Pos.CENTER);
        taskCreatorPane.getChildren().add(durationRow);
    }

    private void setupButtonRow() {
        // Add/Save button
        addSaveButton = new Button("Add");
        updateAddSaveButton();

        // Cancel button
        cancelButton = new Button("Cancel");
        cancelButton.setVisible(false);

        cancelButton.setOnAction(_ -> clearEditingUI());

        // Delete button
        deleteButton = new Button("Delete");
        deleteButton.setVisible(false);

        deleteButton.setOnAction(_ -> {
            projectManager.removeTask(editingTask);
            editingTask = null;
            onTaskChanged.run();
            clearEditingUI();
        });

        // Button HBox
        HBox buttonRow = new HBox(LayoutValues.NODE_SPACING, addSaveButton, cancelButton, deleteButton);

        taskCreatorPane.getChildren().add(buttonRow);
    }

    private void updateAddSaveButton() {
        addSaveButton.setOnAction(_ -> {
            if(editingTask == null) {
                // Create
                try {
                    projectManager.addTask(nameField.getText(), startDatePicker.getValue(), durationSpinner.getValue());
                    onTaskChanged.run();
                } catch (IllegalArgumentException _) {
                    showAlert(
                            "Task name cannot be blank or null.",
                            ""
                    );
                    throw new IllegalArgumentException("Task name cannot be empty.");
                }
            } else {
                // Edit
                try {
                    projectManager.updateTask(editingTask, nameField.getText(), startDatePicker.getValue(), durationSpinner.getValue());
                    clearEditingUI();
                } catch (IllegalArgumentException _) {
                    showAlert(
                            "Task must have a valid start date. \nTask cannot start before the project.",
                            "To change the Project start date: \nFile > Project Settings."
                    );
                }
            }


        });
    }

    private void clearEditingUI() {
        editingTask = null;
        addSaveButton.setText("Add");
        cancelButton.setVisible(false);
        deleteButton.setVisible(false);
        header.setText(CREATE_HEADER);
        onTaskChanged.run();
    }

    public void refreshUI() {
        nameField.setText("Task " + (projectManager.getTasks().size()+1));
        startDatePicker.setValue(projectManager.getStartDate());
        durationSpinner.getValueFactory().setValue(1);
    }

    public void loadTask(Task task) {
        editingTask = task;

        nameField.setText(task.getName());
        startDatePicker.setValue(task.getStartDate());
        durationSpinner.getValueFactory().setValue(task.getDuration());
        String editHeader = "Edit Task";
        header.setText(editHeader);

        addSaveButton.setText("Save");
        cancelButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    private void showAlert(String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

}
