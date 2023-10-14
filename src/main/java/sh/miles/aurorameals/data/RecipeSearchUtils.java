package sh.miles.aurorameals.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Different ways to search recipes
 */
public final class RecipeSearchUtils {

    private RecipeSearchUtils() {

    }

    public static List<Recipe> search(@NotNull final List<Recipe> original, @NotNull final SearchType searchType, @NotNull final String query) {
        List<Recipe> result = new ArrayList<>();
        switch (searchType) {
            case NAME -> result = search(original, (recipe) -> recipe.getName().startsWith(query));
        }
        return result;
    }

    private static List<Recipe> search(@NotNull final List<Recipe> original, Predicate<Recipe> filter) {
        final List<Recipe> filtered = new ArrayList<>();
        for (Recipe recipe : original) {
            if (filter.test(recipe)) {
                filtered.add(recipe);
            }
        }
        return filtered;
    }

    public enum SearchType {
        NAME,
        ;

        @NotNull
        public static SearchType fromString(@NotNull final String str) {
            for (SearchType value : values()) {
                if (value.name().equalsIgnoreCase(str)) {
                    return value;
                }
            }
            return NAME;
        }
    }
}
