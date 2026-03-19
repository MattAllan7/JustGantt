import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class TaskListView {

    private final int ROW_GAP = 5;

    public BorderPane getView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getHBox());
        borderPane.setCenter(getVBox());
        return borderPane;
    }

    private HBox getHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5));
        hBox.setSpacing(5);
        addButtons(hBox);
        return hBox;
    }

    private VBox getVBox() {
        VBox vBox = new VBox(ROW_GAP);

        //vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);
        vBox.getChildren().add(new TextField());
        vBox.getChildren().add(new TextField());

        return vBox;
    }

    private void addButtons(HBox hBox) {

        // Add:

        Button buttonAdd = new Button("Add");

        buttonAdd.setPrefWidth(60);
        buttonAdd.setMinWidth(Region.USE_PREF_SIZE);
        buttonAdd.setMaxWidth(Region.USE_PREF_SIZE);

        buttonAdd.setPrefHeight(25);
        buttonAdd.setMinHeight(Region.USE_PREF_SIZE);
        buttonAdd.setMaxHeight(Region.USE_PREF_SIZE);

        hBox.getChildren().add(buttonAdd);

        // Edit:

        Button buttonEdit = new Button("Edit");

        buttonEdit.setPrefWidth(60);
        buttonEdit.setMinWidth(Region.USE_PREF_SIZE);
        buttonEdit.setMaxWidth(Region.USE_PREF_SIZE);

        buttonEdit.setPrefHeight(25);
        buttonEdit.setMinHeight(Region.USE_PREF_SIZE);
        buttonEdit.setMaxHeight(Region.USE_PREF_SIZE);

        hBox.getChildren().add(buttonEdit);

        // Delete:

        Button buttonDelete = new Button("Delete");

        buttonDelete.setPrefWidth(60);
        buttonDelete.setMinWidth(Region.USE_PREF_SIZE);
        buttonDelete.setMaxWidth(Region.USE_PREF_SIZE);

        buttonDelete.setPrefHeight(25);
        buttonDelete.setMinHeight(Region.USE_PREF_SIZE);
        buttonDelete.setMaxHeight(Region.USE_PREF_SIZE);

        hBox.getChildren().add(buttonDelete);
    }

}
