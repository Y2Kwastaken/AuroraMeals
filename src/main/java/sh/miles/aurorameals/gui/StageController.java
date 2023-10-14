package sh.miles.aurorameals.gui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.Utils;
import sh.miles.aurorameals.gui.custom.Modal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages GUI interactions
 */
public final class GuiManager {

    private static GuiManager instance;

    private final Stage mainStage;
    private final Map<String, Scene> scenes;

    private GuiManager(final Stage mainStage) {
        this.mainStage = mainStage;
        this.scenes = new HashMap<>();
    }

    public void registerScene(@NotNull final String uid, String file) {
        this.scenes.put(uid, new Scene(Utils.loadFxmlFile(getClass(), "application/%s.fxml".formatted(file))));
    }

    @Nullable
    public Scene getScene(@NotNull final String uid) {
        return this.scenes.get(uid);
    }

    @NotNull
    public Modal displayModal(@NotNull final Scene scene, @NotNull final String title) {
        final Modal modal = new Modal(title, scene);
        modal.open();
        return modal;
    }

    @Nullable
    public Modal displayModal(@NotNull final String uid, @NotNull final String title) {
        final Scene scene = getScene(uid);
        if (scene == null) {
            return null;
        }
        final Modal modal = new Modal(title, scene);
        modal.open();
        return modal;
    }

    public static void setInstance(Stage mainStage, Consumer<Stage> setup) {
        setup.accept(mainStage);
        instance = new GuiManager(mainStage);
    }

    public static GuiManager getInstance() {
        return instance;
    }
}
