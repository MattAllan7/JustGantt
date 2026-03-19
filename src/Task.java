import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Task {

    public final int ARC_VALUE = 10;
    public final int PIXELS_PER_DAY = 30;

    private String name;
    private int startDate;
    private int duration;
    private float progress;
    private ArrayList<Task> dependencies;
    private String resources;
    private String description;

    public Task(String name, int startDate, int duration) {
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public Rectangle toRectangle() {
        Rectangle rect = new Rectangle();
        rect.setWidth(getDuration() * PIXELS_PER_DAY);
        rect.setHeight(25);
        rect.setArcWidth(ARC_VALUE);
        rect.setArcHeight(ARC_VALUE);
        return rect;
    }

}
