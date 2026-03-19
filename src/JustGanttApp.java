import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.time.LocalDate;
import java.util.ArrayList;

public class JustGanttApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("one", 0, 1));
        tasks.add(new Task("two", 0, 2));
        tasks.add(new Task("three", 0, 3));
        Project currentProject = new Project("p1", tasks, LocalDate.now());

        primaryStage.setTitle("JustGantt");
        MainView mainView = new MainView();
        primaryStage.setScene(new Scene(mainView.createView(currentProject), 1280, 720));
        primaryStage.setMaximized(true);
        primaryStage.show();

    }
}
