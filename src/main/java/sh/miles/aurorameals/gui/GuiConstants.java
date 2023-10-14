package sh.miles.aurorameals.gui;

import javafx.scene.image.Image;
import sh.miles.aurorameals.util.Utils;

public final class GuiConstants {

    public static final Image ICON = Utils.getImageIcon("icon.png");
    public static final Image DEFAULT_RECIPE_ICON = Utils.getImageIcon("default-recipe-icon.png");

    // Scenes
    public static final String HOME_SCENE = "home";
    public static final String CREATOR_SCENE = "meal-creator";

    private GuiConstants() {
    }
}
