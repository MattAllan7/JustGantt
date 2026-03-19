import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class TimelineView {

    private final int ROW_GAP = 5;
    public final int PIXELS_PER_DAY = 30;
    private final int TASK_RECTANGLE_HEIGHT = 25;
    private final int GAP_BETWEEN_RECTANGLES = 5;

    public BorderPane getView(Project currentProject) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getHBox(currentProject));
        borderPane.setCenter(getTimeline(currentProject));
        return borderPane;
    }

    private HBox getHBox(Project currentProject) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(50);
        hBox.setAlignment(Pos.CENTER_LEFT);

        LocalDate date = currentProject.getStartDate();

        for(int i=0; i<30; i++) {
            Label label = new Label();
            label.setFont(new Font(12));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(PIXELS_PER_DAY);
            label.setMinWidth(PIXELS_PER_DAY);
            label.setMaxWidth(PIXELS_PER_DAY);
            String month = date.getMonth().toString().substring(0, 3);
            label.setText(month + "\n" + date.getDayOfMonth());
            date = date.plusDays(1);
            hBox.getChildren().add(label);
        }

        // temp border code
//        hBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        hBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT,
//                BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
//                CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(5))));
        return hBox;
    }

    private Pane getTimeline(Project currentProject) {
        Pane timeline = new Pane();
        ArrayList<Task> tasks = currentProject.getTasks();
        for(int i=0; i<tasks.size(); i++) {
            Rectangle taskRect = tasks.get(i).toRectangle(PIXELS_PER_DAY, TASK_RECTANGLE_HEIGHT);
            long startDayNumber = ChronoUnit.DAYS.between(LocalDate.now(), tasks.get(i).getStartDate());
            long spaceInPixels = startDayNumber * PIXELS_PER_DAY;

            taskRect.setX(spaceInPixels);
            taskRect.setY((TASK_RECTANGLE_HEIGHT + GAP_BETWEEN_RECTANGLES) * i);

            timeline.getChildren().add(taskRect);
        }
        return timeline;
    }

}
