import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class MainView {

    public BorderPane createView(Project currentProject) {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBarView().getView();
        root.setTop(menuBar);

        BorderPane taskView = new TaskListView().getView();
        root.setLeft(taskView);

        BorderPane timelineView = new TimelineView().getView(currentProject);
        root.setCenter(timelineView);

        return root;
    }

}
