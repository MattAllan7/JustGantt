import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.util.ArrayList;

public class Task {

    private final int ARC_VALUE = 10;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private float progress;
    private ArrayList<Task> dependencies;
    private String resources;
    private String description;

    public Task(String name, LocalDate startDate, int duration) {
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
        endDate = startDate.plusDays(duration);
        progress = 0;
        dependencies = new ArrayList<>();
    }

    public Rectangle toRectangle(int pixelsPerDay) {
        Rectangle rect = new Rectangle();
        rect.setWidth(getDuration() * pixelsPerDay);
        rect.setHeight(25);
        rect.setArcWidth(ARC_VALUE);
        rect.setArcHeight(ARC_VALUE);
        return rect;
    }

    public int getDuration() {
        return duration;
    }

}
