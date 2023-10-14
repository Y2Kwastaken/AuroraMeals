package sh.miles.aurorameals.data.db;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.Main;
import sh.miles.aurorameals.data.Ingredient;
import sh.miles.aurorameals.data.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class RecipesCache {

    private static final RecipesCache instance = new RecipesCache();

    private final List<Recipe> recipes;

    private RecipesCache() {
        this.recipes = new ArrayList<>();
    }

    /**
     * Gets a recipe from the cache
     *
     * @param uuid the uuid of the recipe
     * @return the recipe retrieved
     */
    @Nullable
    public Recipe getRecipe(@NotNull final UUID uuid) {
        for (Recipe recipe : recipes) {
            if (recipe.getUuid().equals(uuid)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Gets a recipe by name from the cache
     *
     * @param name the name of the recipe
     * @return the list of recipes matching the name
     */
    @NotNull
    public List<Recipe> getByName(@NotNull final String name) {
        return getWhichSatisfyName((String recipeName) -> recipeName.equals(name));
    }

    /**
     * Gets a recipe by a condition that can be satisfied by the recipes name
     *
     * @param predicate the predicate which should be satisfied
     * @return a list of matches which are satisfied by the predicate
     */
    @NotNull
    public List<Recipe> getWhichSatisfyName(@NotNull final Predicate<String> predicate) {
        final List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (predicate.test(recipe.getName())) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /**
     * Gets a list of recipes by a condition that can be satisfied by a recipes ingredients
     *
     * @param predicate the predicate which should be satisfied
     * @return a list of matches which are satisfied by the predicate
     */
    @NotNull
    public List<Recipe> getWhichSatisfyIngredient(@NotNull final Predicate<List<Ingredient>> predicate) {
        final List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (predicate.test(recipe.getIngredients())) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /**
     * Adds a recipe to the cache
     *
     * @param recipe the recipe to add
     */
    public void addRecipe(@NotNull final Recipe recipe) {
        Main.LOGGER.info("Registering Recipe %s with name %s".formatted(recipe.getUuid(), recipe.getName()));
        recipes.add(recipe);
    }

    /**
     * Removes a recipe from the cache, and deletes it from the database
     *
     * @param recipe the recipe to remove
     */
    public void removeRecipe(@NotNull final Recipe recipe) {
        recipes.remove(recipe);
        RecipesDatabase.getInstance().deleteRecipe(RecipeDAO.fromRecipe(recipe));
    }

    @NotNull
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(this.recipes);
    }

    public static RecipesCache getInstance() {
        return instance;
    }
}
