package sh.miles.aurorameals.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sh.miles.aurorameals.Main;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.data.cache.RecipeCache;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.aurorameals.gui.StageController;
import sh.miles.aurorameals.gui.custom.IngredientDisplayListCellFactory;
import sh.miles.aurorameals.gui.custom.Modal;
import sh.miles.aurorameals.gui.custom.RecipeDisplayListCellFactory;
import sh.miles.aurorameals.gui.custom.TextAreaDisplayListCellFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MealHomeController implements Initializable {

    @FXML
    private ListView<Recipe> savedRecipesView;
    @FXML
    private Label recipeNameLabel;
    @FXML
    private ImageView selectedRecipeImage;
    @FXML
    private ListView<String> instructionsListView;
    @FXML
    private ListView<Ingredient> ingredientsListView;

    private MealCreatorController mcc;
    private Recipe selected;
    private Modal current;

    public void onRecipeSelect(final MouseEvent event) {
        this.selected = savedRecipesView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateRecipeData(selected);
        }
    }

    public void onCreateRecipe(ActionEvent event) {
        mcc.setViewedRecipe(null);
        if (this.current == null) {
            current = StageController.getInstance().openModal("Recipe Creator", GuiConstants.CREATOR_SCENE, 1200, 600);
            return;
        }
        current.open(1200, 600);
    }

    public void onEditRecipe(ActionEvent event) {
        if (this.selected == null) {
            Main.LOGGER.warn("Unable to edit when no recipe is selected");
            return;
        }

        mcc.setViewedRecipe(this.selected);
        if (current == null) {
            current = StageController.getInstance().openModal("Recipe Creator", GuiConstants.CREATOR_SCENE, 1200, 600);
            return;
        }
        current.open(1200, 600);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savedRecipesView.setCellFactory(new RecipeDisplayListCellFactory()); // view only
        ingredientsListView.setCellFactory(new IngredientDisplayListCellFactory()); // view only
        instructionsListView.setCellFactory(new TextAreaDisplayListCellFactory()); // view only
        savedRecipesView.getItems().addAll(RecipeCache.instance.toList());
        this.selected = null;
        this.mcc = StageController.getInstance().getController(GuiConstants.CREATOR_SCENE);
        RecipeCache.instance.subscribe((item, operation) -> {
            switch (operation) {
                case UPDATE -> updateRecipeData(item);
                case ADD -> savedRecipesView.getItems().add(item);
                case REMOVE -> {
                    savedRecipesView.getItems().remove(item);
                    if (item.equals(selected)) {
                        updateRecipeData(null);
                    }
                }
            }
        });
    }

    private void updateRecipeData(final Recipe updated) {
        if (updated == null) {
            ingredientsListView.getItems().clear();
            instructionsListView.getItems().clear();
            selectedRecipeImage.setImage(GuiConstants.DEFAULT_RECIPE_ICON);
            return;
        }

        if (!selected.equals(updated)) {
            return;
        }

        ingredientsListView.getItems().clear();
        ingredientsListView.getItems().addAll(updated.getIngredients());
        instructionsListView.getItems().clear();
        instructionsListView.getItems().addAll(updated.getInstructions());
        selectedRecipeImage.setImage(updated.getImage());
    }
}
