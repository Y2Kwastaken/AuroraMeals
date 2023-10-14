package sh.miles.aurorameals.gui.custom;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Measurement;

public class IngredientDisplayListCellFactory implements Callback<ListView<Ingredient>, ListCell<Ingredient>> {

    @Override
    public ListCell<Ingredient> call(ListView<Ingredient> ingredientListView) {
        return new IngredientDisplayListCell();
    }

    private static class IngredientDisplayListCell extends ListCell<Ingredient> {

        @Override
        protected void updateItem(Ingredient item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else if (item != null) {
                setText(toReadableString(item));
            }
        }

        private static String toReadableString(@NotNull final Ingredient item) {
            // name\t amount\t measurement\t
            return "%s\t\t%s %s".formatted(item.getName(), item.getAmountAsFraction(), item.getMeasurement() == Measurement.NONE ? item.getMeasurement() : item.getMeasurement().toString() + "(s)");
        }
    }
}
