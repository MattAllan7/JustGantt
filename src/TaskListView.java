import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TaskListView {

    private final int TASK_TEXT_HEIGHT = 25;
    private final int GAP_BETWEEN_TEXT = 5;

    public BorderPane getView(Project currentProject) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getHBox());
        borderPane.setCenter(getVBox(currentProject));
        return borderPane;
    }

    private HBox getHBox() {
        HBox hBox = new HBox();
        hBox.setPrefHeight(50);
        hBox.setAlignment(Pos.CENTER_LEFT);

        addButtons(hBox);

        return hBox;
    }

    private VBox getVBox(Project currentProject) { // Change to Pane
        VBox vBox = new VBox(GAP_BETWEEN_TEXT);

        ArrayList<Task> tasks = currentProject.getTasks();
        for(int i=0; i<tasks.size(); i++) {
            Label label = new Label(tasks.get(i).getName());
            label.setFont(new Font(12));
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPrefHeight(TASK_TEXT_HEIGHT);

            vBox.getChildren().add(label);
        }
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
