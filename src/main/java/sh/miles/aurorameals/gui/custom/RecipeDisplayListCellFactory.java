package sh.miles.aurorameals.gui.custom;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sh.miles.aurorameals.data.Recipe;

public class RecipeDisplayListCellFactory implements Callback<ListView<Recipe>, ListCell<Recipe>> {

    @Override
    public ListCell<Recipe> call(ListView<Recipe> recipeListView) {
        return new RecipeListCell();
    }

    private static class RecipeListCell extends ListCell<Recipe> {

        @Override
        protected void updateItem(Recipe item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getName());
            }
        }
    }

}
