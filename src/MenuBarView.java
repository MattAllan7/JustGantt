import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * The menu bar at the top containing items such as "File".
 */
public class MenuBarView {

    private final ProjectManager projectManager;
    private final Runnable onProjectChanged;
    private final Consumer<String> onThemeChanged;
    private final UserPreferences userPreferences;

    public MenuBarView(ProjectManager projectManager, Runnable onProjectChanged, Consumer<String> onThemeChanged, UserPreferences userPreferences) {
        this.projectManager = projectManager;
        this.onProjectChanged = onProjectChanged;
        this.onThemeChanged = onThemeChanged;
        this.userPreferences = userPreferences;
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
        newItem.setOnAction(_ -> handleNew());

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(_ -> handleOpen(menuBar.getScene().getWindow()));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(_ -> handleSave(menuBar.getScene().getWindow()));

        MenuItem saveAsItem = new MenuItem("Save As");
        saveAsItem.setOnAction(_ -> handleSaveAs(menuBar.getScene().getWindow()));

        MenuItem projectSettingsItem = new MenuItem("Project Settings");
        projectSettingsItem.setOnAction(_ -> handleProjectSettings("Save"));

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, projectSettingsItem);

        return fileMenu;
    }

    private Menu buildEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(_ -> handlePreferences());

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
        handleProjectSettings("Create");
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
                showAlert("Failed to open file:\n" + e.getMessage());
            }
        }
    }

    /**
     * Shows an alert when trying to discard a project that is not saved.
     *
     * @return True if the user accepts the discard, false if they select cancel.
     */
    private boolean confirmCloseProject(String title, String header, String context) {
        if(!projectManager.hasFilePath() && !projectManager.getTasks().isEmpty()) {
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
                showAlert("Failed to save:\n" + e.getMessage());
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
                showAlert("Failed to save:\n" + e.getMessage());
            }
        }
    }

    /**
     * Handles the "Project Settings" menu action.
     * Opens a new stage, allowing the user to set a new project start date.
     */
    private void handleProjectSettings(String saveCreateButtonName) {
        // Setup stage
        Stage projectSettingsStage = new Stage();
        projectSettingsStage.setTitle("Project Settings");
        projectSettingsStage.initModality(Modality.APPLICATION_MODAL);

        // Rename
        TextField renameField = new TextField(projectManager.getProjectName());
        HBox renameRow = buildRenameRow(renameField);

        // New start date
        DatePicker datePicker = new DatePicker(projectManager.getStartDate());
        HBox dateRow = buildDateRow(datePicker);

        // Buttons
        HBox buttonRow = buildProjectSettingsButtons(projectSettingsStage, renameField, datePicker, saveCreateButtonName);

        // Output pane
        VBox outputPane = buildProjectSettingsPane(renameRow, dateRow, buttonRow);

        // Output stage
        projectSettingsStage.setScene(new Scene(outputPane));
        projectSettingsStage.setResizable(false);
        projectSettingsStage.show();

    }

    private HBox buildRenameRow(TextField renameField) {
        HBox renameRow = new HBox(LayoutValues.NODE_SPACING);

        renameField.setMinWidth(LayoutValues.FIELD_WIDTH);
        renameField.setPrefWidth(LayoutValues.FIELD_WIDTH);
        renameField.setMaxWidth(LayoutValues.FIELD_WIDTH);

        Label renameLabel = new Label("Name:");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        renameRow.getChildren().addAll(renameLabel, spacer, renameField);
        renameRow.setAlignment(Pos.CENTER_LEFT);

        return renameRow;
    }

    private HBox buildDateRow(DatePicker datePicker) {
        HBox dateRow = new HBox();

        datePicker.setMinWidth(LayoutValues.FIELD_WIDTH);
        datePicker.setPrefWidth(LayoutValues.FIELD_WIDTH);
        datePicker.setMaxWidth(LayoutValues.FIELD_WIDTH);

        Label dateLabel = new Label("Start Date:");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        dateRow.getChildren().addAll(dateLabel, spacer, datePicker);
        dateRow.setAlignment(Pos.CENTER_LEFT);

        return dateRow;
    }

    private HBox buildProjectSettingsButtons(Stage projectSettingsStage, TextField projectNameField, DatePicker datePicker, String saveCreateButtonName) {
        HBox buttonRow = new HBox(LayoutValues.NODE_SPACING);

        Button saveCreateButton = new Button(saveCreateButtonName);
        Button cancelButton = new Button("Cancel");

        saveCreateButton.setOnAction(_ -> {
            String projectName = projectNameField.getText();
            LocalDate newStartDate = datePicker.getValue();

            try {
                projectManager.setProjectName(projectName);
                projectManager.setStartDate(newStartDate);

                onProjectChanged.run();
                projectSettingsStage.close();
            } catch(IllegalArgumentException e) {
                showAlert(e.getMessage());
            }
        });

        cancelButton.setOnAction(_ -> projectSettingsStage.close());

        buttonRow.getChildren().addAll(saveCreateButton, cancelButton);

        return buttonRow;
    }

    private VBox buildProjectSettingsPane(HBox renameRow, HBox dateRow, HBox buttonRow) {
        VBox projectSettingsPane = new VBox(LayoutValues.POPUP_NODE_SPACING, renameRow, dateRow, buttonRow);
        projectSettingsPane.setPadding(new Insets(LayoutValues.POPUP_NODE_SPACING));
        projectSettingsPane.setPrefWidth(LayoutValues.POPUP_WIDTH);

        return projectSettingsPane;
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

        // Radio buttons
        RadioButton darkRadio = new RadioButton("Dark");
        RadioButton lightRadio = new RadioButton("Light");
        HBox themePane = buildPreferencesRadios(darkRadio, lightRadio);

        // Buttons
        HBox buttonRow = buildPreferencesButtons(preferencesStage, darkRadio);

        // Output pane
        VBox preferencesPane = buildPreferencesPane(themePane, buttonRow);

        // Output stage
        preferencesStage.setScene(new Scene(preferencesPane));
        preferencesStage.setResizable(false);
        preferencesStage.show();
    }

    private HBox buildPreferencesRadios(RadioButton darkRadio, RadioButton lightRadio) {
        // Read current saved theme so the correct radio button starts selected.
        String currentTheme = userPreferences.getTheme();

        // Build radio buttons.
        ToggleGroup themeToggleGroup = new ToggleGroup();

        darkRadio.setToggleGroup(themeToggleGroup);
        darkRadio.setSelected(UserPreferences.THEME_DARK.equals(currentTheme));

        lightRadio.setToggleGroup(themeToggleGroup);
        lightRadio.setSelected(UserPreferences.THEME_LIGHT.equals(currentTheme));

        VBox radioBox = new VBox(LayoutValues.NODE_SPACING, darkRadio, lightRadio);

        // Build pane.
        Label themeLabel = new Label("Theme:");

        HBox themePane = new HBox(LayoutValues.POPUP_NODE_SPACING, themeLabel, radioBox);
        themePane.setAlignment(Pos.CENTER_LEFT);

        return themePane;
    }

    private HBox buildPreferencesButtons(Stage preferencesStage, RadioButton darkRadio) {

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(_ -> {
            String chosen = darkRadio.isSelected() ? UserPreferences.THEME_DARK : UserPreferences.THEME_LIGHT;
            onThemeChanged.accept(chosen);
            preferencesStage.close();
        });

        cancelButton.setOnAction(_ -> preferencesStage.close());

        return new HBox(LayoutValues.NODE_SPACING, saveButton, cancelButton);
    }

    private VBox buildPreferencesPane(HBox themePane, HBox buttonRow) {
        VBox preferencesPane = new VBox(LayoutValues.POPUP_NODE_SPACING, themePane, buttonRow);
        preferencesPane.setPadding(new Insets(LayoutValues.POPUP_NODE_SPACING));
        preferencesPane.setPrefWidth(LayoutValues.POPUP_WIDTH);

        return preferencesPane;
    }

    private void showAlert(String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(header);
        alert.setContentText(null);
        System.err.println(header);
        alert.showAndWait();
    }

}
