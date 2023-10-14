package sh.miles.aurorameals.gui.custom;

import com.google.common.base.Preconditions;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Modal {

    private final String title;
    private final Scene scene;
    private Runnable onClose;
    private Stage stage;

    private Modal(final String title, final Scene scene, @Nullable final Runnable onClose) {
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(scene);
        this.title = title;
        this.scene = scene;
        this.onClose = onClose;
    }

    public void open() {
        open(1200, 600);
    }

    public void open(int width, int height) {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setTitle(this.title);
        addWindowHandler(() -> onClose != null);
        stage.show();
    }

    public String getTitle() {
        return title;
    }

    public void setOnClose(@NotNull final Runnable onClose) {
        Preconditions.checkNotNull(onClose);
        addWindowHandler(() -> this.onClose == null);
        this.onClose = onClose;
    }

    private void onWindowClose(@NotNull final WindowEvent event) {
        onClose.run();
        this.stage.getScene().getWindow().removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this::onWindowClose);
    }

    private void addWindowHandler(Supplier<Boolean> bf) {
        if (bf.get()) {
            stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this::onWindowClose);
        }
    }

    public static Modal open(@NotNull final String title, @NotNull final Scene scene, @NotNull final Runnable onClose) {
        final Modal modal = new Modal(title, scene, onClose);
        modal.open();
        return modal;
    }

    public static Modal open(@NotNull final String title, @NotNull final Scene scene) {
        final Modal modal = new Modal(title, scene, null);
        modal.open();
        return modal;
    }

    public static Modal open(@NotNull final String title, @NotNull final Scene scene, int width, int height) {
        final Modal modal = new Modal(title, scene, null);
        modal.open(width, height);
        return modal;
    }

    public static Modal create(@NotNull final String title, @NotNull final Scene scene, @Nullable Runnable onClose) {
        return new Modal(title, scene, onClose);
    }

    public static Modal create(@NotNull final String title, @NotNull final Scene scene) {
        return new Modal(title, scene, null);
    }
}
