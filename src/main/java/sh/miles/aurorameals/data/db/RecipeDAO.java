package sh.miles.aurorameals.data.db;

import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.util.Utils;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.raven.datamapper.v2.DataContainer;
import sh.miles.raven.datamapper.v2.data.DataField;
import sh.miles.raven.datamapper.v2.data.DataTuple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * A Data access object for Recipes which contain instructions and ingredients
 */
public class RecipeDAO implements DataContainer {

    public static final String NAME_DEFAULT = "default";

    private final DataTuple<UUID> uuid;
    private final DataField<String> name;
    private final DataField<byte[]> imageBytes;
    private final DataField<List<byte[]>> ingredients;
    private final DataField<List<String>> instructions;

    public RecipeDAO(@NotNull final UUID uuid) {
        this.uuid = new DataTuple<>("uuid", uuid);
        this.name = new DataField<>("name", NAME_DEFAULT);
        this.imageBytes = new DataField<>("image-bytes", Utils.getImageBytes(GuiConstants.DEFAULT_RECIPE_ICON));
        this.ingredients = new DataField<>("ingredients", new ArrayList<>());
        this.instructions = new DataField<>("instructions", new ArrayList<>());
    }

    public RecipeDAO() {
        this(UUID.randomUUID());
    }

    @NotNull
    public UUID getUniqueId() {
        return this.uuid.getValue();
    }


    @NotNull
    public String getName() {
        return this.name.getValue();
    }

    public void setName(@NotNull final String name) {
        this.name.setValue(name);
    }

    public byte[] getImageBytes() {
        return this.imageBytes.getValue();
    }

    public void setImageBytes(final byte @NotNull [] bytes) {
        this.imageBytes.setValue(bytes);
    }

    @NotNull
    public List<byte[]> getIngredients() {
        return this.ingredients.getValue();
    }

    public void setIngredients(@NotNull final List<byte[]> encodedIngredients) {
        this.ingredients.setValue(encodedIngredients);
    }

    @NotNull
    public List<String> getInstructions() {
        return this.instructions.getValue();
    }

    public void setInstructions(@NotNull final List<String> instructions) {
        this.instructions.setValue(instructions);
    }

    @NotNull
    public static Recipe toRecipe(@NotNull final RecipeDAO recipeDAO) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (byte[] ingredient : recipeDAO.getIngredients()) {
            ingredients.add(Ingredient.deserialize(ingredient));
        }
        return new Recipe(recipeDAO.getUniqueId(), recipeDAO.getName(), Utils.getImageFromBytes(recipeDAO.getImageBytes()), new LinkedList<>(ingredients), new LinkedList<>(recipeDAO.getInstructions()));
    }

    @NotNull
    public static RecipeDAO fromRecipe(@NotNull final Recipe recipe) {
        final List<byte[]> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.add(ingredient.serialize());
        }
        final RecipeDAO recipeDAO = new RecipeDAO(recipe.getUuid());
        recipeDAO.setName(recipe.getName());
        recipeDAO.setImageBytes(Utils.getImageBytes(recipe.getImage()));
        recipeDAO.setIngredients(ingredients);
        recipeDAO.setInstructions(recipe.getInstructions());
        return recipeDAO;
    }
}
