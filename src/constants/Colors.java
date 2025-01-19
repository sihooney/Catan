package constants;

import java.awt.*;
import java.util.HashMap;

public class Colors {

    public static final Color[] COLORS = {Color.RED, Color.BLUE, Color.ORANGE, Color.WHITE};
    public static final String[] NAMES = {"RED", "BLUE", "ORANGE", "WHITE"};
    public static final HashMap<Color, String> COLOR_TO_STRING = new HashMap<>() {{
        put(Color.RED, "RED");
        put(Color.BLUE, "BLUE");
        put(Color.ORANGE, "ORANGE");
        put(Color.WHITE, "WHITE");
    }};
}
