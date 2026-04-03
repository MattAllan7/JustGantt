import javafx.geometry.Pos;
import javafx.scene.paint.Color;
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

    public final int PIXELS_PER_DAY = 40;

    private ProjectManager projectManager;
    private int rowGap;
    private int rowHeight;

    private HBox dateAxis;
    private Pane rectangleArea;
    private ScrollPane headerScrollPane;
    private ScrollPane contentScrollPane;

    public TimelineView(ProjectManager projectManager, int rowGap, int rowHeight) {
        this.projectManager = projectManager;
        this.rowGap = rowGap;
        this.rowHeight = rowHeight;

        setupDateAxis();
        setupRectangleArea();
        refreshUI();
    }

    public ScrollPane getScrollPane() {
        return contentScrollPane;
    }

    public VBox getView() {
        VBox container = new VBox(headerScrollPane, contentScrollPane);
        VBox.setVgrow(contentScrollPane, Priority.ALWAYS);
        return container;
    }

    private void setupDateAxis() {
        dateAxis = new HBox();
        dateAxis.setMinHeight(50);
        dateAxis.setPrefHeight(50);
        dateAxis.setAlignment(Pos.CENTER);

        headerScrollPane = new ScrollPane(dateAxis);
        headerScrollPane.setMinHeight(50);
        headerScrollPane.setPrefHeight(50);
        headerScrollPane.setMaxHeight(50);
        headerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        headerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        headerScrollPane.setFitToHeight(true);
        headerScrollPane.setFitToWidth(false);
    }

    private void setupRectangleArea() {
        rectangleArea = new Pane();

        contentScrollPane = new ScrollPane(rectangleArea);
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // Do NOT setFitToHeight — that would disable vertical scrolling
        contentScrollPane.setFitToHeight(false);
        contentScrollPane.setFitToWidth(false);

        // Keep dateAxis scroll position in sync with the content horizontal scroll
        contentScrollPane.hvalueProperty().addListener((obs, oldVal, newVal) ->
                headerScrollPane.setHvalue(newVal.doubleValue())
        );
    }

    public void refreshUI() {
        double timelineWidth = getTimelineWidth();

        dateAxis.setPrefWidth(timelineWidth);
        rectangleArea.setPrefWidth(timelineWidth);

        refreshDateAxis();
        refreshRectangleArea();
    }

    private void refreshDateAxis() {
        dateAxis.getChildren().clear();

        LocalDate date = projectManager.getStartDate();
        int minimumDaysShown = 25;
        int projectLength = projectManager.getProjectLength();
        int datesShown = Math.max(minimumDaysShown, projectLength) + 5;
        for(int i=0; i<datesShown; i++) {
            // Label formatting
            Label label = new Label();
            label.setFont(new Font(12));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(PIXELS_PER_DAY);
            label.setMinWidth(PIXELS_PER_DAY);
            label.setMaxWidth(PIXELS_PER_DAY);

            // Label text and displaying
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

    private double getTimelineWidth() {
        int minimumDaysShown = 25;
        int projectLength = projectManager.getProjectLength();
        int datesShown = Math.max(minimumDaysShown, projectLength) + 5;

        return datesShown * PIXELS_PER_DAY;
    }

    private Rectangle toRectangle(Task task, int index) {
        int arcValue = 10;

        Rectangle rect = new Rectangle();
        rect.setWidth(task.getDuration() * PIXELS_PER_DAY);
        rect.setHeight(rowHeight);
        rect.setArcWidth(arcValue);
        rect.setArcHeight(arcValue);
        rect.setFill(Color.DODGERBLUE);

        long startDayNumber = ChronoUnit.DAYS.between(projectManager.getStartDate(), task.getStartDate());
        long spaceInPixels = startDayNumber * PIXELS_PER_DAY;

        rect.setX(spaceInPixels);
        rect.setY((rowHeight + rowGap) * index);

        return rect;
    }

}
