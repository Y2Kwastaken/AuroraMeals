package sh.miles.aurorameals.gui.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.Main;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.data.db.RecipesCache;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.aurorameals.gui.StageController;
import sh.miles.aurorameals.gui.custom.IngredientDisplayListCellFactory;
import sh.miles.aurorameals.gui.custom.Modal;
import sh.miles.aurorameals.gui.custom.RecipeDisplayListCellFactory;
import sh.miles.aurorameals.gui.custom.TextAreaDisplayListCellFactory;

import java.net.URL;
import java.util.ArrayList;
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


    private Recipe currentRecipe;
    private Modal current;

    public void onRecipeSelect(final MouseEvent event) {
        final Recipe recipe = savedRecipesView.getSelectionModel().getSelectedItem();
        if (recipe != null) {
            selectRecipe(recipe);
        }
    }

    public void onCreateRecipe(ActionEvent event) {
        final MealCreatorController mcc = StageController.getInstance().getController(GuiConstants.CREATOR_SCENE);
        mcc.setViewedRecipe(null);
        if (current == null) {
            current = StageController.getInstance().openModal("Recipe Creator", GuiConstants.CREATOR_SCENE, 1200, 600);
            return;
        }
        current.open(1200, 600);
    }

    public void onEditRecipe(ActionEvent event) {
        if (this.currentRecipe == null) {
            Main.LOGGER.warn("Unable to edit null recipe");
            return;
        }

        final MealCreatorController mcc = StageController.getInstance().getController(GuiConstants.CREATOR_SCENE);
        mcc.setViewedRecipe(this.currentRecipe);
        if (current == null) {
            current = StageController.getInstance().openModal("Recipe Creator", GuiConstants.CREATOR_SCENE, 1200, 600);
            return;
        }
        current.open(1200, 600);
    }

    public void selectRecipe(Recipe recipe) {
        if (recipe == null) {
            recipeNameLabel.setText("");
            selectedRecipeImage.setImage(GuiConstants.DEFAULT_RECIPE_ICON);
            ingredientsListView.getItems().clear();
            instructionsListView.getItems().clear();
        }

        if (recipe.equals(this.currentRecipe)) {
            return;
        }
        this.currentRecipe = recipe;
        refreshRecipe();
    }

    public void refreshRecipe() {
        if (this.currentRecipe == null) {
            Main.LOGGER.warn("Was unable to refresh null current recipe");
            return;
        }

        recipeNameLabel.setText(this.currentRecipe.getName());
        selectedRecipeImage.setImage(this.currentRecipe.getImage());
        ingredientsListView.setItems(FXCollections.observableList(new ArrayList<>()));
        ingredientsListView.getItems().addAll(this.currentRecipe.getIngredients());
        instructionsListView.setItems(FXCollections.observableList(new ArrayList<>()));
        instructionsListView.getItems().addAll(this.currentRecipe.getInstructions());
    }

    public void updateRecipeList() {
        savedRecipesView.getItems().clear();
        savedRecipesView.getItems().addAll(RecipesCache.getInstance().getAllRecipes());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savedRecipesView.setCellFactory(new RecipeDisplayListCellFactory()); // view only
        ingredientsListView.setCellFactory(new IngredientDisplayListCellFactory()); // view only
        instructionsListView.setCellFactory(new TextAreaDisplayListCellFactory()); // view only
        savedRecipesView.getItems().addAll(RecipesCache.getInstance().getAllRecipes());
    }
}
