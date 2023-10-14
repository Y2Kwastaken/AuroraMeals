package sh.miles.aurorameals.gui.custom;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Measurement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngredientListCellFactory implements Callback<ListView<Ingredient>, ListCell<Ingredient>> {

    private static final Pattern CHARACTER_PERMISSION_PATTERN = Pattern.compile("[^\\d\\/ ]");

    @Override
    public ListCell<Ingredient> call(ListView<Ingredient> ingredientListView) {
        return new IngredientListCell();
    }

    private static class IngredientListCell extends ListCell<Ingredient> {

        @Override
        protected void updateItem(Ingredient ingredient, boolean empty) {
            super.updateItem(ingredient, empty);
            if (ingredient == null) {
                setGraphic(null);
            } else {
                setGraphic(createGraphic(ingredient));
            }
        }

        @NotNull
        static HBox createGraphic(@Nullable Ingredient ingredient) {
            // TODO: update ingredient you clown
            final HBox box = new HBox();
            final TextField name = new TextField(ingredient == null ? null : ingredient.getName().isBlank() ? null : ingredient.getName());
            name.setOnKeyReleased((event) -> ingredient.setName(name.getText()));
            name.setPromptText("Ingredient Name");
            final TextField amount = new TextField(ingredient == null ? null : ingredient.getAmountAsFraction().equals("0") ? null : ingredient.getAmountAsFraction());
            amount.setPromptText("1/5");
            amount.textProperty().addListener((ObservableValue<? extends String> value, String oldValue, String newValue) -> {
                if (newValue == null || newValue.isBlank()) {
                    return;
                }

                if (newValue.length() > 4) {
                    amount.setText(oldValue);
                    return;
                }

                final Matcher matcher = CHARACTER_PERMISSION_PATTERN.matcher(newValue);
                if (matcher.find()) {
                    amount.setText(oldValue);
                    return;
                }

                String[] split = newValue.split("/");
                ingredient.setAmount(new byte[]{Byte.parseByte(split[0]), split.length == 2 ? Byte.parseByte(split[1]) : 0});
            });
            final ComboBox<Measurement> measurement = new ComboBox<>();
            measurement.valueProperty().addListener((ObservableValue<? extends Measurement> value, Measurement oldValue, Measurement newValue) -> {
                ingredient.setMeasurement(newValue);
            });
            measurement.getItems().addAll(Measurement.values());
            measurement.getSelectionModel().select(ingredient == null ? Measurement.NONE : ingredient.getMeasurement());
            box.getChildren().addAll(name, amount, measurement);
            return box;
        }
    }
}
