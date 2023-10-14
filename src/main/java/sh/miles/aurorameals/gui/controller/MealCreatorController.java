package sh.miles.aurorameals.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sh.miles.aurorameals.Main;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Measurement;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.data.db.RecipesCache;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.aurorameals.gui.StageController;
import sh.miles.aurorameals.gui.custom.IngredientListCellFactory;
import sh.miles.aurorameals.gui.custom.TextAreaListCellFactory;
import sh.miles.aurorameals.util.MutableContainer;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MealCreatorController implements Initializable {

    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField recipeNameField;
    @FXML
    private ListView<MutableContainer<String>> instructionListView;
    @FXML
    private ImageView recipeImage;
    @FXML
    private ListView<Ingredient> ingredientListView;
    private Recipe recipe;

    public void onImageClick(MouseEvent event) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg", "*.webp"));
        choose.setTitle("Choose A Recipe Image");
        StageController.getInstance().withWindow((Window window) -> {
            final File file = choose.showOpenDialog(window);
            if (file == null) {
                return;
            }
            this.recipeImage.setImage(new Image(file.toURI().toString()));
        });
    }

    public void onRecipeAdd(ActionEvent actionEvent) {
        ingredientListView.getItems().add(new Ingredient("", new byte[]{0, 0}, Measurement.NONE));
    }

    public void onIngredientListKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && ingredientListView.getSelectionModel().getSelectedItem() != null) {
            ingredientListView.getItems().remove(ingredientListView.getSelectionModel().getSelectedItem());
        }
    }

    public void onInstructionAdd(ActionEvent event) {
        instructionListView.getItems().add(MutableContainer.of(""));
    }

    public void onInstructionsKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && instructionListView.getSelectionModel().getSelectedItem() != null) {
            instructionListView.getItems().remove(instructionListView.getSelectionModel().getSelectedItem());
        }
    }

    public void onRecipeSave(ActionEvent event) {
        if (recipe == null) {
            Recipe.Builder builder = Recipe.builder()
                    .setName(recipeNameField.getText())
                    .setImage(recipeImage.getImage());
            for (MutableContainer<String> item : instructionListView.getItems()) {
                builder.addInstruction(item.get());
            }
            for (Ingredient item : ingredientListView.getItems()) {
                builder.addIngredient(item);
            }
            this.recipe = builder.build();
            RecipesCache.getInstance().addRecipe(this.recipe);
        } else {
            recipe.setInstructions(new LinkedList<>(instructionListView.getItems().stream().map(MutableContainer::get).toList()));
            recipe.setIngredients(new LinkedList<>(ingredientListView.getItems()));
            recipe.setName(recipeNameField.getText());
            recipe.setImage(recipeImage.getImage());
            Main.LOGGER.info("Updated Recipe Information");
        }

        final MealHomeController mhc = StageController.getInstance().getController(GuiConstants.HOME_SCENE);
        mhc.refreshRecipe();
        mhc.updateRecipeList();

        ((Stage) (saveButton.getScene().getWindow())).close();
    }

    public void onRecipeDelete(ActionEvent event) {
        if (recipe == null) {
            setViewedRecipe(null);
        }

        RecipesCache.getInstance().removeRecipe(this.recipe);
        final MealHomeController mhc = StageController.getInstance().getController(GuiConstants.HOME_SCENE);
        mhc.updateRecipeList();
        mhc.selectRecipe(null);
        ((Stage) deleteButton.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredientListView.setCellFactory((ListView<Ingredient> view) -> new IngredientListCellFactory().call(view));
        instructionListView.setCellFactory((ListView<MutableContainer<String>> view) -> new TextAreaListCellFactory().call(view));
        setViewedRecipe(null);
    }

    public void setViewedRecipe(final Recipe recipe) {
        instructionListView.getItems().clear();
        ingredientListView.getItems().clear();
        recipeNameField.clear();
        if (recipe == null) {
            recipeImage.setImage(GuiConstants.DEFAULT_RECIPE_ICON);
            return;
        }

        instructionListView.getItems().addAll(recipe.getInstructions().stream().map(MutableContainer::of).toList());
        ingredientListView.getItems().addAll(recipe.getIngredients());
        recipeImage.setImage(recipe.getImage());
        recipeNameField.setText(recipe.getName());

        this.recipe = recipe;
    }

}
