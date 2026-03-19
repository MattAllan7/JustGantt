import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TimelineView {

    private final int ROW_GAP = 5;
    public final int PIXELS_PER_DAY = 30;

    public BorderPane getView(Project currentProject) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getHBox(currentProject));
        borderPane.setCenter(getVBox(currentProject));
        return borderPane;
    }

    private HBox getHBox(Project currentProject) {
        HBox hBox = new HBox();
        //hBox.setPadding(new Insets(5));
        LocalDate date = currentProject.getStartDate();

        for(int i=0; i<30; i++) {
            Label label = new Label();
            label.setFont(new Font(12));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setMinWidth(PIXELS_PER_DAY);
            label.setPrefWidth(PIXELS_PER_DAY);
            label.setMaxWidth(PIXELS_PER_DAY);
            String month = date.getMonth().toString().substring(0, 3);
            label.setText(month + "\n" + date.getDayOfMonth());
            date = date.plusDays(1);
            hBox.getChildren().add(label);

//            Text text = new Text();
//            text.setTextAlignment(TextAlignment.CENTER);
//            text.setFont(new Font(12));
//            String month = date.getMonth().toString().substring(0, 3);
//            text.setText(month + "\n" + date.getDayOfMonth());
//            date = date.plusDays(i);
//            hBox.getChildren().add(text);
        }

        //hBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        hBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT,
//                BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
//                CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(5))));
        return hBox;
    }

    private VBox getVBox(Project currentProject) {
        VBox vBox = new VBox(ROW_GAP);
        vBox.setSpacing(5);
        //vBox.setPadding(new Insets(5));
        for(Task task : currentProject.getTasks()) {
            vBox.getChildren().add(task.toRectangle());
        }
        return vBox;
    }

}
