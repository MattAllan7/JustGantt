import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The menu bar at the top containing items such as "File".
 */
public class MenuBarView {

    private ProjectManager projectManager;
    private Runnable onProjectChanged;

    public MenuBarView(ProjectManager projectManager, Runnable onProjectChanged) {
        this.projectManager = projectManager;
        this.onProjectChanged = onProjectChanged;
    }

    /**
     * Builds and returns the application menu bar.
     * Contains a "File" menu with New, Open, Save, and Save As items.
     *
     * @return The configured MenuBar to be placed at the top of the main view.
     */
    public MenuBar getView() {
        MenuBar menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(true);

        Menu file = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
            handleNew();
        });

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> {
            handleOpen(menuBar.getScene().getWindow());
        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> {
            handleSave(menuBar.getScene().getWindow());
        });

        MenuItem saveAsItem = new MenuItem("Save As");
        saveAsItem.setOnAction(e -> {
            handleSaveAs(menuBar.getScene().getWindow());
        });

        MenuItem projectSettingsItem = new MenuItem("Project Settings");
        projectSettingsItem.setOnAction(e -> {
            handleProjectSettings();
        });

        file.getItems().addAll(newItem, openItem, saveItem, saveAsItem, projectSettingsItem);
        menuBar.getMenus().add(file);

        return menuBar;
    }

    /**
     * Creates a FileChooser with a filter for .gantt project files.
     *
     * @return A FileChooser restricted to Gantt project files (*.gantt).
     */
    private FileChooser buildFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gantt Project Files (*.gantt)", "*.gantt")
        );
        return fileChooser;
    }

    /**
     * Handles the "New" menu action.
     * If the current project has never been saved,
     * prompts the user to confirm discarding unsaved changes before proceeding.
     * Then opens a text input dialogue to collect the new project's name.
     * If a non-blank name is entered, a new project is created and all views are refreshed.
     */
    private void handleNew() {
        // If there's unsaved work, ask for confirmation.
        if(!projectManager.hasFilePath()) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("New Project");
            confirmAlert.setHeaderText("Unsaved changes will be lost.");
            confirmAlert.setContentText("Create a new project anyway?");
            if(confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK)
                return;
        }

        // Create new project window.
        TextInputDialog nameDialog = new TextInputDialog("Untitled");
        nameDialog.setTitle("New Project");
        nameDialog.setHeaderText(null);
        nameDialog.setContentText("Project name:");
        String name = nameDialog.showAndWait().orElse("").trim();

        // Create the new project if a name was entered.
        if(!name.isBlank()) {
            projectManager.newProject(name, LocalDate.now());
            onProjectChanged.run();
        }
    }

    /**
     * Handles the "Open" menu action.
     * Shows an open-file dialogue filtered to .gantt files.
     * If the user selects a file, loads the project from that path and refreshes all views.
     * Displays an error alert if loading fails.
     *
     * @param window The owner window for the file dialogue.
     */
    private void handleOpen(Window window) {
        // Create FileChooser
        FileChooser chooser = buildFileChooser();
        chooser.setTitle("Open Project");
        File file = chooser.showOpenDialog(window);

        // If the user selected a .gantt file, try and load it and update UI.
        if(file != null) {
            try {
                projectManager.loadFrom(file.getAbsolutePath());
                onProjectChanged.run();
            } catch(Exception e) {
                showError("Failed to open file:\n" + e.getMessage());
            }
        }
    }

    /**
     * Handles the "Save" menu action.
     * If the project already has an associated file path,
     * saves directly to that path without prompting.
     * If no path has been set yet, runs handleSaveAs() instead.
     * Displays an error alert if saving fails.
     *
     * @param window The owner window for the file dialogue, used if Save As is required.
     */
    private void handleSave(Window window) {
        if(projectManager.hasFilePath()) {
            try {
                projectManager.save();
            } catch(Exception e) {
                showError("Failed to save:\n" + e.getMessage());
            }
        } else {
            handleSaveAs(window);
        }
    }

    /**
     * Handles the "Save As" menu action.
     * Shows a save-file dialogue with the current project name pre-filled as the suggested filename.
     * Appends the .gantt extension if the chosen path is missing it.
     * Saves the project to the chosen path and displays an error alert if saving fails.
     *
     * @param window The owner window for the file dialogue.
     */
    private void handleSaveAs(Window window) {
        FileChooser chooser = buildFileChooser();
        chooser.setTitle("Save Project As");
        chooser.setInitialFileName(projectManager.getProjectName() + ".gantt");
        File file = chooser.showSaveDialog(window);

        if(file != null) {
            String path = file.getAbsolutePath();
            if(!path.endsWith(".gantt")) {
                path += ".gantt";
            }
            try {
                projectManager.saveAs(path);
                onProjectChanged.run();
            } catch(Exception e) {
                showError("Failed to save:\n" + e.getMessage());
            }
        }
    }

    /**
     * Handles the "Project Settings" menu action.
     * Opens a new stage, allowing the user to set a new project start date.
     */
    private void handleProjectSettings() {
        Stage projectSettingsStage = new Stage();
        projectSettingsStage.setTitle("Project Settings");
        projectSettingsStage.initModality(Modality.APPLICATION_MODAL);

        // Start date row
        Label startDateLabel = new Label("Project Start Date:");
        DatePicker startDatePicker = new DatePicker(projectManager.getStartDate());

        HBox startDateRow = new HBox(startDateLabel, startDatePicker);
        startDateRow.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            LocalDate newStartDate = startDatePicker.getValue();

            if(!checkStartDateValidity(newStartDate))
                return;

            projectManager.setStartDate(newStartDate);
            onProjectChanged.run();
            projectSettingsStage.close();
        });

        cancelButton.setOnAction(e -> {
            projectSettingsStage.close();
        });

        HBox buttonRow = new HBox(saveButton, cancelButton);

        // Layout
        VBox layout = new VBox(10, startDateRow, buttonRow);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(350);

        projectSettingsStage.setScene(new Scene(layout));
        projectSettingsStage.setResizable(false);
        projectSettingsStage.show();

    }

    /**
     * Checks whether a new project start date is valid by:
     *  Ensuring the date is not null,
     *  Ensuring the date is not after existing task dates.
     * Shows a corresponding alert message if the date is not allowed.
     *
     * @param projectStartDate The new start date being considered.
     * @return TRUE if the date is valid, FALSE if it is not.
     */
    private boolean checkStartDateValidity(LocalDate projectStartDate) {

        if(projectStartDate == null) {
            showError("Invalid start date. ");
            return false;
        }

        ArrayList<Task> tasks = projectManager.getTasks();
        for(Task task : tasks) {
            if(task.getStartDate().isBefore(projectStartDate)) {
                showError("Project start date must be before or the same date as all tasks. ");
                return false;
            }
        }

        return true;
    }

    /**
     * Displays an error alert with the given message.
     *
     * @param message The error message to display to the user.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
