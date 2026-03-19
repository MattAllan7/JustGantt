import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.util.ArrayList;

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
        addButtons(hBox);
        return hBox;
    }

    private VBox getVBox() {
        VBox vBox = new VBox(ROW_GAP);

        vBox.getChildren().add(new TextField());
        vBox.getChildren().add(new TextField());

        return vBox;
    }

    private void addButtons(HBox hBox) {

        ArrayList<Button> buttons = new ArrayList<>();

        Button buttonAdd = new Button("Add");
        buttons.add(buttonAdd);

        Button buttonEdit = new Button("Edit");
        buttons.add(buttonEdit);

        Button buttonDelete = new Button("Delete");
        buttons.add(buttonDelete);

        for(Button button : buttons) {
            button.setPrefWidth(60);
            button.setMinWidth(Region.USE_PREF_SIZE);
            button.setMaxWidth(Region.USE_PREF_SIZE);

            button.setPrefHeight(25);
            button.setMinHeight(Region.USE_PREF_SIZE);
            button.setMaxHeight(Region.USE_PREF_SIZE);
        }

        hBox.getChildren().addAll(buttons);
    }

}
