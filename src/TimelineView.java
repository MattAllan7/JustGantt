import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    private final ProjectManager projectManager;

    private HBox dateAxis;
    private Pane rectangleArea;
    private ScrollPane headerScrollPane;
    private ScrollPane contentScrollPane;

    public TimelineView(ProjectManager projectManager) {
        this.projectManager = projectManager;

        setupDateAxis();
        setupRectangleArea();

        headerScrollPane.hvalueProperty().bindBidirectional(
                contentScrollPane.hvalueProperty()
        );

        refreshUI();
    }

    /**
     * Used to bind to the timeline scrollbar.
     *
     * @return The task list scroll pane.
     */
    public ScrollPane getScrollPane() {
        return contentScrollPane;
    }

    public VBox getView() {
        VBox vBox = new VBox(LayoutValues.NODE_SPACING, headerScrollPane, contentScrollPane);
        vBox.setPadding(new Insets(LayoutValues.NODE_SPACING));
        VBox.setVgrow(contentScrollPane, Priority.ALWAYS);
        return vBox;
    }

    private void setupDateAxis() {
        dateAxis = new HBox();
        dateAxis.setMinHeight(LayoutValues.HEADER_HEIGHT);
        dateAxis.setPrefHeight(LayoutValues.HEADER_HEIGHT);
        dateAxis.setMaxHeight(LayoutValues.HEADER_HEIGHT);
        dateAxis.setAlignment(Pos.TOP_CENTER);

        headerScrollPane = new ScrollPane(dateAxis);
        headerScrollPane.setMinHeight(LayoutValues.HEADER_HEIGHT);
        headerScrollPane.setPrefHeight(LayoutValues.HEADER_HEIGHT);
        headerScrollPane.setMaxHeight(LayoutValues.HEADER_HEIGHT);
        headerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        headerScrollPane.setFitToHeight(true);
        headerScrollPane.setFitToWidth(false);
    }

    private void setupRectangleArea() {
        rectangleArea = new Pane();

        contentScrollPane = new ScrollPane(rectangleArea);
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        contentScrollPane.setFitToHeight(false);
        contentScrollPane.setFitToWidth(false);
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
        int projectLength = projectManager.getProjectLength();
        int datesShown = Math.max(LayoutValues.MINIMUM_DAYS, projectLength) + LayoutValues.EXTRA_DAYS;
        for(int i=0; i<datesShown; i++) {
            // Label formatting
            Label label = new Label();
            label.setFont(LayoutValues.NORMAL_FONT);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(LayoutValues.PIXELS_PER_DAY);
            label.setMinWidth(LayoutValues.PIXELS_PER_DAY);
            label.setMaxWidth(LayoutValues.PIXELS_PER_DAY);

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
        int projectLength = projectManager.getProjectLength();
        int datesShown = Math.max(LayoutValues.MINIMUM_DAYS, projectLength) + LayoutValues.EXTRA_DAYS;

        return datesShown * LayoutValues.PIXELS_PER_DAY;
    }

    private Rectangle toRectangle(Task task, int index) {
        Rectangle rect = new Rectangle();
        rect.setWidth(task.getDuration() * LayoutValues.PIXELS_PER_DAY);
        rect.setHeight(LayoutValues.ROW_HEIGHT);
        rect.setArcWidth(LayoutValues.RECTANGLE_ARC);
        rect.setArcHeight(LayoutValues.RECTANGLE_ARC);
        rect.setFill(Color.DODGERBLUE);

        long startDayNumber = ChronoUnit.DAYS.between(projectManager.getStartDate(), task.getStartDate());
        double spaceInPixels = startDayNumber * LayoutValues.PIXELS_PER_DAY;

        rect.setX(spaceInPixels);
        rect.setY((LayoutValues.ROW_HEIGHT + LayoutValues.NODE_SPACING) * index);

        return rect;
    }

}