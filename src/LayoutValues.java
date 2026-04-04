import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LayoutValues {

    public final static double NODE_SPACING = 5.0;
    public final static double ROW_HEIGHT = 30.0;
    public final static double PIXELS_PER_DAY = 40.0;
    public final static double RECTANGLE_ARC = 10.0;
    public final static double HEADER_HEIGHT = 50.0;
    public final static double FIELD_WIDTH = 200.0;
    public final static double POPUP_WIDTH = 350.0;
    public final static double POPUP_NODE_SPACING = 20.0;

    public final static int MINIMUM_DAYS = 25;
    public final static int EXTRA_DAYS = 5;

    private final static double HEADER_FONT_SIZE = 24.0;
    public final static Font HEADER_FONT = Font.font("System", FontWeight.BOLD, LayoutValues.HEADER_FONT_SIZE);

    private final static double NORMAL_FONT_SIZE = 12.0;
    public final static Font NORMAL_FONT = Font.font("System", FontWeight.NORMAL, LayoutValues.NORMAL_FONT_SIZE);

    private LayoutValues() {} // Prevent accidental instantiation.

}
