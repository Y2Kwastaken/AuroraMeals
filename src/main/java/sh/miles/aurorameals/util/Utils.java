package sh.miles.aurorameals.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Utils {

    private Utils() {
    }

    public static Pair<Parent, Object> loadFxmlFile(Class<?> clazz, @NotNull final String resource) {
        try {
            final FXMLLoader loader = new FXMLLoader(clazz.getClassLoader().getResource(resource));
            return Pair.of(loader.load(), loader.getController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadStyleSheet(Class<?> clazz, @NotNull final String resource) {
        return clazz.getClassLoader().getResource(resource).toExternalForm();
    }

    public static Image getImageIcon(@NotNull final String resource) {
        return new Image(Utils.class.getClassLoader().getResourceAsStream(resource));
    }

    public static int[] getImageDimensions(@NotNull final Image image) {
        return new int[]{(int) image.getWidth(), (int) image.getHeight()};
    }

    public static byte[] getImageBytes(@NotNull final Image image) {
        final BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getImageFromBytes(final byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            return new Image(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void centerStage(final Stage stage, double width, double height) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

    }

}
