package sh.miles.aurorameals.gui.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.aurorameals.data.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class RecipeView {

    private static final List<RecipeViewListener> listeners = new ArrayList<>();

    private final String label;
    private Recipe recipe;

    public RecipeView(@NotNull final String label) {
        this(label, null);
    }

    public RecipeView(@NotNull final String label, @Nullable Recipe recipe) {
        this.label = label;
        this.recipe = recipe;
    }

    public void update(@NotNull final Consumer<Recipe> consumer) {
        consumer.accept(recipe);
        watch(label, recipe);
    }

    public void set(Recipe recipe) {
        this.recipe = recipe;
        watch(label, recipe);
    }

    public static void subscribe(RecipeViewListener listener) {
        listeners.add(listener);
    }

    private static void watch(@NotNull final String label, @Nullable final Recipe recipe) {
        for (RecipeViewListener listener : listeners) {
            listener.onRecipeChange(label, recipe);
        }
    }

}
