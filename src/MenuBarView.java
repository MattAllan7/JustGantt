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
import java.util.function.Consumer;

/**
 * The menu bar at the top containing items such as "File".
 */
public class MenuBarView {

    private ProjectManager projectManager;
    private Runnable onProjectChanged;
    private Consumer<String> onThemeChanged;

    public MenuBarView(ProjectManager projectManager, Runnable onProjectChanged, Consumer<String> onThemeChanged) {
        this.projectManager = projectManager;
        this.onProjectChanged = onProjectChanged;
        this.onThemeChanged = onThemeChanged;
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

        Menu fileMenu = buildFileMenu(menuBar);
        Menu editMenu = buildEditMenu();

        menuBar.getMenus().addAll(fileMenu, editMenu);

        return menuBar;
    }

    private Menu buildFileMenu(MenuBar menuBar) {
        Menu fileMenu = new Menu("File");

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

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, projectSettingsItem);

        return fileMenu;
    }

    private Menu buildEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(e -> {
            handlePreferences();
        });

        editMenu.getItems().add(preferencesItem);

        return editMenu;
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
        String title = "New Project";
        String header = "Unsaved changes will be lost";
        String context = "Create a new project anyway?";
        if(confirmCloseProject(title, header, context)) return;

        // Create new empty project.
        projectManager.newProject("Untitled", LocalDate.now());
        onProjectChanged.run();
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
                // If there's unsaved work, ask for confirmation.
                String title = "Open Project";
                String header = "Unsaved changes will be lost";
                String context = "Open an existing project anyway?";
                if(confirmCloseProject(title, header, context)) return;

                projectManager.loadFrom(file.getAbsolutePath());
                onProjectChanged.run();
            } catch(Exception e) {
                showError("Failed to open file:\n" + e.getMessage());
            }
        }
    }

    /**
     * Shows an alert when trying to discard a project that is not saved.
     *
     * @return True if the user accepts the discard, false if they select cancel.
     */
    private boolean confirmCloseProject(String title, String header, String context) {
        if(!projectManager.hasFilePath()) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle(title);
            confirmAlert.setHeaderText(header);
            confirmAlert.setContentText(context);
            return confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK;
        }
        return false;
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
        // Setup stage
        Stage projectSettingsStage = new Stage();
        projectSettingsStage.setTitle("Project Settings");
        projectSettingsStage.initModality(Modality.APPLICATION_MODAL);

        // Rename
        TextField renameField = new TextField(projectManager.getProjectName());
        HBox renameRow = getRenameRow(renameField);

        // New start date
        DatePicker datePicker = new DatePicker(projectManager.getStartDate());
        HBox dateRow = getDateRow(datePicker);

        // Buttons
        HBox buttonRow = getButtonRow(projectSettingsStage, renameField, datePicker);

        // Output pane
        VBox outputPane = getOutputPane(renameRow, dateRow, buttonRow);

        // Output stage
        projectSettingsStage.setScene(new Scene(outputPane));
        projectSettingsStage.setResizable(false);
        projectSettingsStage.show();

    }

    private HBox getRenameRow(TextField renameField) {
        HBox renameRow = new HBox();

        Label renameLabel = new Label("Rename Project:");

        renameRow.getChildren().addAll(renameLabel, renameField);
        renameRow.setAlignment(Pos.CENTER_LEFT);

        return renameRow;
    }

    private HBox getDateRow(DatePicker datePicker) {
        HBox dateRow = new HBox();

        Label dateLabel = new Label("Start Date:");

        dateRow.getChildren().addAll(dateLabel, datePicker);
        dateRow.setAlignment(Pos.CENTER_LEFT);

        return dateRow;
    }

    private HBox getButtonRow(Stage projectSettingsStage, TextField projectNameField, DatePicker datePicker) {
        HBox buttonRow = new HBox();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String projectName = projectNameField.getText();
            LocalDate newStartDate = datePicker.getValue();

            if(!checkProjectNameValidity(projectName)) return;
            if(!checkStartDateValidity(newStartDate)) return;

            projectManager.setProjectName(projectName);
            projectManager.setStartDate(newStartDate);

            onProjectChanged.run();
            projectSettingsStage.close();
        });

        cancelButton.setOnAction(e -> {
            projectSettingsStage.close();
        });

        buttonRow.getChildren().addAll(saveButton, cancelButton);

        return buttonRow;
    }

    private VBox getOutputPane(HBox renameRow, HBox dateRow, HBox buttonRow) {
        VBox outputPane = new VBox(10, renameRow, dateRow, buttonRow);
        outputPane.setPadding(new Insets(20));
        outputPane.setPrefWidth(350);
        return outputPane;
    }

    private boolean checkProjectNameValidity(String projectName) {
        return !projectName.isBlank();
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
     * Handles the "Preferences" menu action.
     * Opens a modal dialog with Light / Dark radio buttons.
     * Applies and persists the chosen theme immediately on Save.
     */
    private void handlePreferences() {
        Stage preferencesStage = new Stage();
        preferencesStage.setTitle("Preferences");
        preferencesStage.initModality(Modality.APPLICATION_MODAL);
        preferencesStage.setResizable(false);

        // Read current saved theme so the correct radio button starts selected.
        UserPreferences userPreferences = new UserPreferences();
        String currentTheme = userPreferences.getTheme();

        // Radio buttons
        ToggleGroup themeGroup = new ToggleGroup();

        RadioButton darkRadio  = new RadioButton("Dark");
        darkRadio.setToggleGroup(themeGroup);
        darkRadio.setSelected(UserPreferences.THEME_DARK.equals(currentTheme));

        RadioButton lightRadio = new RadioButton("Light");
        lightRadio.setToggleGroup(themeGroup);
        lightRadio.setSelected(UserPreferences.THEME_LIGHT.equals(currentTheme));

        VBox radioBox = new VBox(8, darkRadio, lightRadio);

        Label themeLabel = new Label("Theme:");
        HBox themeRow = new HBox(12, themeLabel, radioBox);
        themeRow.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String chosen = darkRadio.isSelected() ? UserPreferences.THEME_DARK : UserPreferences.THEME_LIGHT;
            onThemeChanged.accept(chosen);
            preferencesStage.close();
        });

        cancelButton.setOnAction(e -> {
            preferencesStage.close();
        });

        HBox buttonRow = new HBox(8, saveButton, cancelButton);

        // Output pane
        VBox outputPane = new VBox(16, themeRow, buttonRow);
        outputPane.setPadding(new Insets(20));
        outputPane.setPrefWidth(220);

        // Output stage
        preferencesStage.setScene(new Scene(outputPane));
        preferencesStage.show();
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
