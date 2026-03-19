import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.util.ArrayList;

public class Task {

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

    public Rectangle toRectangle(int pixelsPerDay, int rectHeight) {
        int arcValue = 10;

        Rectangle rect = new Rectangle();
        rect.setWidth(getDuration() * pixelsPerDay);
        rect.setHeight(rectHeight);
        rect.setArcWidth(arcValue);
        rect.setArcHeight(arcValue);
        return rect;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }

}
