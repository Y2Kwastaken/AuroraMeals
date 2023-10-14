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
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Measurement;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.data.cache.RecipeCache;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.aurorameals.gui.StageController;
import sh.miles.aurorameals.gui.custom.IngredientListCellFactory;
import sh.miles.aurorameals.gui.custom.TextAreaListCellFactory;
import sh.miles.aurorameals.util.MutableContainer;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
            Recipe.Builder builder = Recipe.builder();
            builder.setName(recipeNameField.getText());
            builder.setImage(recipeImage.getImage());
            for (MutableContainer<String> item : instructionListView.getItems()) {
                builder.addInstruction(item.get());
            }
            for (Ingredient item : ingredientListView.getItems()) {
                builder.addIngredient(item);
            }

            this.recipe = builder.build();
            RecipeCache.instance.add(this.recipe);
        } else {
            RecipeCache.instance.operate(this.recipe.getUuid(), (recipe) -> {
                recipe.setName(recipeNameField.getText());
                recipe.setImage(recipeImage.getImage());
                recipe.setInstructions(new LinkedList<>(instructionListView.getItems().stream().map(MutableContainer::get).toList()));
                recipe.setIngredients(new LinkedList<>(ingredientListView.getItems()));
            });
        }

        ((Stage) saveButton.getScene().getWindow()).close();
    }

    public void onRecipeDelete(ActionEvent event) {
        if (this.recipe == null) {
            updateView();
        } else {
            RecipeCache.instance.remove(this.recipe);
        }

        ((Stage) deleteButton.getScene().getWindow()).close();
    }

    public void setViewedRecipe(@Nullable final Recipe recipe) {
        this.recipe = recipe;
        updateView();
    }

    private void updateView() {
        recipeNameField.clear();
        ingredientListView.getItems().clear();
        instructionListView.getItems().clear();
        if (this.recipe == null) {
            recipeImage.setImage(GuiConstants.DEFAULT_RECIPE_ICON);
            return;
        }

        recipeNameField.setText(recipe.getName());
        ingredientListView.getItems().addAll(recipe.getIngredients());
        instructionListView.getItems().addAll(recipe.getInstructions().stream().map(MutableContainer::of).collect(Collectors.toList()));
        recipeImage.setImage(recipeImage.getImage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredientListView.setCellFactory((ListView<Ingredient> view) -> new IngredientListCellFactory().call(view));
        instructionListView.setCellFactory((ListView<MutableContainer<String>> view) -> new TextAreaListCellFactory().call(view));
        this.recipe = null;
    }

}
