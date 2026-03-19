import javafx.scene.control.*;

public class MenuBarView {

    public MenuBar getView() {
        MenuBar menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(true);

        Menu file = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(event -> {
            // On click logic.
            System.out.println("New");
        });

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(event -> {
            // On click logic.
            System.out.println("Open");
        });

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(event -> {
            // On click logic.
            System.out.println("Save");
        });

        file.getItems().addAll(newItem, openItem, saveItem);
        menuBar.getMenus().add(file);

        return menuBar;
    }

}
