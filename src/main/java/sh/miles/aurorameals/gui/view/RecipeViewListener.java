package sh.miles.aurorameals.gui.view;

import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.data.Recipe;

@FunctionalInterface
public interface RecipeViewListener {
    void onRecipeChange(@NotNull final String label, @NotNull final Recipe recipe);
}
