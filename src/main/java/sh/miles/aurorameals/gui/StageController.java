package sh.miles.aurorameals.gui;

import com.google.common.base.Preconditions;
import com.pixelduke.transit.Style;
import com.pixelduke.transit.TransitTheme;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.util.Utils;
import sh.miles.aurorameals.gui.custom.Modal;
import sh.miles.aurorameals.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages GUI interactions
 */
public final class StageController {

    private static StageController instance;

    private final Stage mainStage;
    private final Map<String, Pair<Scene, Object>> scenes;

    private StageController(final Stage mainStage) {
        this.mainStage = mainStage;
        this.scenes = new HashMap<>();
    }

    public void registerScene(@NotNull final String uid, String file, @Nullable String... cssFiles) {
        final Pair<Parent, Object> obj = Utils.loadFxmlFile(getClass(), "application/%s.fxml".formatted(file));
        final Scene scene = new Scene(obj.getFirst());
        for (String cssFile : cssFiles) {
            scene.getStylesheets().add(Utils.loadStyleSheet(getClass(), "css/%s.css".formatted(cssFile)));
        }
        // final TransitTheme metro = new TransitTheme(Style.LIGHT);
        // metro.setScene(scene);
        this.scenes.put(uid, Pair.of(scene, obj.getSecond()));
    }

    @Nullable
    public Scene getScene(@NotNull final String sceneUid) {
        return this.scenes.get(sceneUid).getFirst();
    }

    @Nullable
    public <T> T getController(@NotNull final String sceneUid) {
        return (T) this.scenes.get(sceneUid).getSecond();
    }

    public void withWindow(@NotNull final Consumer<Window> window) {
        window.accept(this.mainStage.getScene().getWindow());
    }

    public Modal openModal(@NotNull final String title, @NotNull final String sceneUid, int width, int height) {
        final Scene scene = getScene(sceneUid);
        Preconditions.checkNotNull(scene);
        return Modal.open(title, scene, width, height);
    }

    public Modal openModal(@NotNull final String title, @NotNull final String sceneUid) {
        final Scene scene = getScene(sceneUid);
        Preconditions.checkNotNull(scene);
        return Modal.open(title, scene);
    }

    public static void setInstance(Stage mainStage, Consumer<Stage> setup) {
        setup.accept(mainStage);
        instance = new StageController(mainStage);
    }

    public static StageController getInstance() {
        return instance;
    }
}
