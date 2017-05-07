package uml.controllers.colorUtilities;

import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Bron: Kris Coolsaet https://github.ugent.be/kcoolsae/Prog2
 *
 * Class that makes it possible to search the name of a color.
 * Does this by initializing a map with (color, name)-pairs.
 */

public class WebColorNames {

    private static final Map<Color,String> NAMES = initMap ();

    public static String toName (Color color) {
        return NAMES.get(color);
    }

    public static Map<Color,String> initMap () {
        Map<Color,String> map = new HashMap<>();
        for (Field field : Color.class.getFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) &&
                    Modifier.isStatic(modifiers) &&
                    Modifier.isPublic(modifiers) &&
                    field.getType() == Color.class) {
                try {
                    map.put ((Color)field.get(null), field.getName().toLowerCase());
                } catch (IllegalAccessException e) {
                    // Ignore fields without value
                }
            }
        }
        return map;
    }

}