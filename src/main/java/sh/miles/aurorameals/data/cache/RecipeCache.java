package sh.miles.aurorameals.data.cache;

import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.data.Recipe;
import sh.miles.aurorameals.data.db.RecipeDAO;
import sh.miles.aurorameals.data.db.RecipesDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class RecipeCache extends SimpleCache<UUID, Recipe> {

    public static final RecipeCache instance = new RecipeCache();

    public void add(@NotNull final Recipe recipe) {
        add(recipe.getUuid(), recipe);
    }

    public void remove(@NotNull final Recipe recipe) {
        remove(recipe.getUuid());
        RecipesDatabase.getInstance().deleteRecipe(RecipeDAO.fromRecipe(recipe));
    }

    @Override
    public @NotNull List<Recipe> toList() {
        final List<Recipe> recipes = new ArrayList<>();
        cache.forEach((k, v) -> recipes.add(v.copy()));
        return recipes;
    }

    @Override
    public @NotNull List<Recipe> toList(Predicate<Recipe> filter) {
        final List<Recipe> recipes = new ArrayList<>();
        cache.forEach((k, v) -> {
            if (filter.test(v)) {
                recipes.add(v.copy());
            }
        });
        return recipes;
    }
}
