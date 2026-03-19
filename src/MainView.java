import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class MainView {

    public BorderPane createView(Project currentProject) {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBarView().getView();
        root.setTop(menuBar);

        BorderPane taskView = new TaskListView().getView();
        taskView.setPadding(new Insets(5));
        root.setLeft(taskView);

        BorderPane timelineView = new TimelineView().getView(currentProject);
        timelineView.setPadding(new Insets(5));
        root.setCenter(timelineView);

        // test, temporary
        DatePicker datePicker = new DatePicker();
        root.setRight(datePicker);

        return root;
    }

}
