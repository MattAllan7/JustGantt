import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * The right view.
 * Displays the timeline or chart of the project.
 * The date axis is at the top and the tasks are represented as rectangles.
 */
public class TimelineView {

    public final int PIXELS_PER_DAY = 50;

    private ProjectManager projectManager;
    private int rowGap;
    private int rowHeight;

    private HBox dateAxis;
    private Pane rectangleArea;

    public TimelineView(ProjectManager projectManager, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.rowGap = rowGap;
        this.rowHeight = rowHeight;

        setupDateAxis();
        setupRectangleArea();
        refreshUI();
    }

    private void setupDateAxis() {
        dateAxis = new HBox();
        dateAxis.setPrefHeight(50);
        dateAxis.setAlignment(Pos.CENTER); // Regarding the y-axis.
    }

    private void setupRectangleArea() {
        rectangleArea = new Pane();
    }

    public BorderPane getView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(dateAxis);
        borderPane.setCenter(rectangleArea);
        return borderPane;
    }

    public void refreshUI() {
        refreshDateAxis();
        refreshRectangleArea();
    }

    private void refreshDateAxis() {
        dateAxis.getChildren().clear();

        LocalDate date = projectManager.getStartDate();
        for(int i=0; i<30; i++) {
            Label label = new Label();
            label.setFont(new Font(12));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(PIXELS_PER_DAY);
            label.setMinWidth(PIXELS_PER_DAY);
            label.setMaxWidth(PIXELS_PER_DAY);
            String month = date.getMonth().toString().substring(0, 3);
            label.setText(month + "\n" + date.getDayOfMonth());
            date = date.plusDays(1);
            dateAxis.getChildren().add(label);
        }
    }

    private void refreshRectangleArea() {
        rectangleArea.getChildren().clear();

        ArrayList<Task> tasks = projectManager.getTasks();
        for(int i=0; i<tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            Rectangle taskRect = toRectangle(currentTask, i);
            rectangleArea.getChildren().add(taskRect);
        }
    }

    private Rectangle toRectangle(Task task, int index) {
        int arcValue = 10;

        Rectangle rect = new Rectangle();
        rect.setWidth(task.getDuration() * PIXELS_PER_DAY);
        rect.setHeight(rowHeight);
        rect.setArcWidth(arcValue);
        rect.setArcHeight(arcValue);

        long startDayNumber = ChronoUnit.DAYS.between(projectManager.getStartDate(), task.getStartDate());
        long spaceInPixels = startDayNumber * PIXELS_PER_DAY;

        rect.setX(spaceInPixels);
        rect.setY((rowHeight + rowGap) * index); // index likely needs to change eventually.

        return rect;
    }

}
