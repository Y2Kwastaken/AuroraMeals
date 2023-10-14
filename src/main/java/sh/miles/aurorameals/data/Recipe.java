package sh.miles.aurorameals.data;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

/**
 * A recipe covers a set of data that covers a cooking recipe
 */
public class Recipe {

    private final UUID uuid;
    private String name;
    private Image image;
    private LinkedList<Ingredient> ingredients;
    private LinkedList<String> instructions;

    public Recipe(@NotNull final UUID uuid, @NotNull final String name, @NotNull final Image image, @NotNull final LinkedList<Ingredient> ingredients, @NotNull final LinkedList<String> instructions) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.ingredients = new LinkedList<>(ingredients);
        this.instructions = new LinkedList<>(instructions);
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @NotNull
    public LinkedList<Ingredient> getIngredients() {
        return new LinkedList<>(ingredients);
    }

    public void setIngredients(LinkedList<Ingredient> ingredients) {
        this.ingredients = new LinkedList<>(ingredients);
    }

    @NotNull
    public LinkedList<String> getInstructions() {
        return new LinkedList<>(instructions);
    }

    public void setInstructions(LinkedList<String> instructions) {
        this.instructions = new LinkedList<>(instructions);
    }

    public boolean hasIngredient(@NotNull final Ingredient ingredient) {
        return ingredients.contains(ingredient);
    }

    public boolean hasIngredient(@NotNull final String ingredientName) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    public Recipe copy() {
        return new Recipe(uuid, name, image, ingredients, instructions);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Recipe recipe)) return false;
        return Objects.equals(uuid, recipe.uuid) && Objects.equals(name, recipe.name) && Objects.equals(image, recipe.image) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(instructions, recipe.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, image, ingredients, instructions);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final UUID uuid = UUID.randomUUID();
        private String name;
        private Image image;
        private final LinkedList<Ingredient> ingredients = new LinkedList<>();
        private final LinkedList<String> instructions = new LinkedList<>();

        private Builder() {
        }

        public Builder setName(@NotNull final String name) {
            this.name = name;
            return this;
        }

        public Builder setImage(Image image) {
            this.image = image;
            return this;
        }

        public Builder addIngredient(@NotNull final Ingredient ingredient) {
            this.ingredients.add(ingredient);
            return this;
        }

        public Builder addInstruction(@NotNull final String instruction) {
            this.instructions.add(instruction);
            return this;
        }

        public boolean canBuild() {
            return name != null && !ingredients.isEmpty() && !instructions.isEmpty();
        }

        public Recipe build() {
            return new Recipe(uuid, name, image, ingredients, instructions);
        }
    }
}
