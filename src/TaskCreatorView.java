import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The left view.
 * Allows the user to enter various fields to create a new Task.
 */
public class TaskCreatorView {

    private ProjectManager projectManager;
    private Runnable onTaskChanged;
    private int rowGap;

    private Task editingTask = null;
    private Button addSaveButton;
    private Button cancelButton;
    private Button deleteButton;
    private final String CREATE_HEADER = "Create New Task";
    private final String EDIT_HEADER = "Edit Task";

    private Label header;
    private VBox taskCreatorPane;

    private TextField nameField;
    private DatePicker startDatePicker;
    private Spinner<Integer> durationSpinner;

    public TaskCreatorView(ProjectManager projectManager, Runnable onTaskChanged, int rowGap) {
        this.projectManager = projectManager;
        this.onTaskChanged = onTaskChanged;
        this.rowGap = rowGap;

        setupHeader();
        setupTaskCreatorPane();
    }

    public VBox getView() {
        VBox vBox = new VBox();

        vBox.getChildren().addAll(header, taskCreatorPane);
        return vBox;
    }

    private void setupHeader() {
        header = new Label(CREATE_HEADER);
        header.setFont(Font.font("System", FontWeight.BOLD, 24));
        header.setPrefHeight(50); // Get all formatting from MainView eventually.
    }

    private void setupTaskCreatorPane() {
        taskCreatorPane = new VBox(rowGap);

        setupNameRow();
        setupStartDateRow();
        setupDurationRow();
        setupButtonRow();
    }

    private void setupNameRow() {
        HBox nameRow = new HBox();
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        taskCreatorPane.getChildren().add(nameRow);
    }

    private void setupStartDateRow() {
        HBox startDateRow = new HBox();
        Label startDateLabel = new Label("Start Date:");
        startDatePicker = new DatePicker(projectManager.getStartDate());
        startDateRow.getChildren().addAll(startDateLabel, startDatePicker);
        taskCreatorPane.getChildren().add(startDateRow);
    }

    private void setupDurationRow() {
        HBox durationRow = new HBox();
        Label durationLabel = new Label("Duration:");

        int minValue = 1;
        int maxValue = 100;
        int initialValue = 1;
        durationSpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue);
        durationSpinner.setValueFactory(valueFactory);
        durationSpinner.setEditable(true);

        durationRow.getChildren().addAll(durationLabel, durationSpinner);
        taskCreatorPane.getChildren().add(durationRow);
    }

    private void setupButtonRow() {
        // <<< Add/Save button >>>
        addSaveButton = new Button("Add");
        updateAddSaveButton();

        // <<< Cancel button >>>
        cancelButton = new Button("Cancel");
        cancelButton.setVisible(false);

        cancelButton.setOnAction(e -> {
            clearEditingUI();
        });

        // <<< Delete button >>>
        deleteButton = new Button("Delete");
        deleteButton.setVisible(false);

        deleteButton.setOnAction(e -> {
            projectManager.removeTask(editingTask);
            editingTask = null;
            onTaskChanged.run();
            clearEditingUI();
        });

        // <<< Button HBox >>>
        HBox buttonRow = new HBox(addSaveButton, cancelButton, deleteButton);

        taskCreatorPane.getChildren().add(buttonRow);
    }

    private void updateAddSaveButton() {
        addSaveButton.setOnAction(e -> {
            if(editingTask == null) {
                // Create
                projectManager.addTask(nameField.getText(), startDatePicker.getValue(), durationSpinner.getValue());
            } else {
                // Edit
                projectManager.updateTask(editingTask, nameField.getText(), startDatePicker.getValue(), durationSpinner.getValue());
                clearEditingUI();
            }

            onTaskChanged.run();
        });
    }

    private void clearEditingUI() {
        editingTask = null;
        addSaveButton.setText("Add");
        cancelButton.setVisible(false);
        deleteButton.setVisible(false);
        header.setText(CREATE_HEADER);
    }

    public void refreshUI() {
        nameField.clear();
        startDatePicker.setValue(projectManager.getStartDate());
        durationSpinner.getValueFactory().setValue(1);
    }

    public void loadTask(Task task) {
        editingTask = task;

        nameField.setText(task.getName());
        startDatePicker.setValue(task.getStartDate());
        durationSpinner.getValueFactory().setValue(task.getDuration());
        header.setText(EDIT_HEADER);

        addSaveButton.setText("Save");
        cancelButton.setVisible(true);
        deleteButton.setVisible(true);
    }

}
