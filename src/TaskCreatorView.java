import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

    private void setupHeader() {
        header = new Label("Create New Task");
        header.setFont(Font.font("System", FontWeight.BOLD, 24));
        header.setPrefHeight(50); // Get all formatting from MainView eventually.
    }

    private void setupTaskCreatorPane() {
        taskCreatorPane = new VBox(rowGap);

        // <<< Name row >>>
        HBox nameRow = new HBox();
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        taskCreatorPane.getChildren().add(nameRow);

        // <<< StartDate row >>>
        HBox startDateRow = new HBox();
        Label startDateLabel = new Label("Start Date:");
        startDatePicker = new DatePicker(projectManager.getStartDate());
        startDateRow.getChildren().addAll(startDateLabel, startDatePicker);
        taskCreatorPane.getChildren().add(startDateRow);

        // <<< Duration row >>>
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

        // <<< Add button >>>
        Button addButton = new Button("Add");
        taskCreatorPane.getChildren().add(addButton);

        addButton.setOnAction(e -> {
            Task task = new Task(nameField.getText(), startDatePicker.getValue(), durationSpinner.getValue());
            projectManager.addTask(task);
            onTaskChanged.run();
        });
    }

    public BorderPane getView() {
        BorderPane borderPane = new BorderPane();

        borderPane.setPrefWidth(400);
        borderPane.setMinWidth(Region.USE_PREF_SIZE);
        borderPane.setMaxWidth(Region.USE_PREF_SIZE);

        borderPane.setTop(header);
        borderPane.setCenter(taskCreatorPane);
        return borderPane;
    }

    public void refreshUI() {
        nameField.clear();
        startDatePicker.setValue(projectManager.getStartDate());
        durationSpinner.getValueFactory().setValue(1);
    }

}
