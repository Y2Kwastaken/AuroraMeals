package sh.miles.aurorameals.gui.custom;

import com.google.common.base.Preconditions;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

public class CustomModal {

    private final String title;
    private final Scene scene;
    private Stage stage;

    public CustomModal(final String title, final Scene scene) {
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(scene);
        this.title = title;
        this.scene = scene;
    }

    public boolean open() {
        return open(true);
    }

    public boolean open(final boolean cache) {
        if (cache && stage != null) {
            if (stage.isShowing()) {
                stage.requestFocus();
            } else {
                stage.show();
            }
            return false;
        }

        if (cache) {
            stage = new Stage();
        }

        stage.setTitle(this.title);
        stage.setScene(this.scene);
        stage.getScene().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onWindowClose);
        stage.show();
        return true;
    }

    private void onWindowClose(@NotNull final WindowEvent event) {
        stage = null;
        System.out.println("de-referenced stage");
    }

}
