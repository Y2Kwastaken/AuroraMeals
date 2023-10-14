package sh.miles.aurorameals.dao;

import org.jetbrains.annotations.NotNull;
import sh.miles.raven.datamapper.api.DataContainer;
import sh.miles.raven.datamapper.api.fields.DataField;
import sh.miles.raven.datamapper.api.fields.DataTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Data access object for Recipes which contain instructions and ingredients
 */
public class RecipeDAO implements DataContainer {

    public static final String NAME_DEFAULT = "default";

    private final DataTuple<UUID> uuid = new DataTuple<>("uuid", UUID.randomUUID());
    private final DataField<String> name = new DataField<>("name", NAME_DEFAULT);
    private final DataField<List<byte[]>> ingredients = new DataField<>("ingredients", new ArrayList<>());
    private final DataField<List<String>> instructions = new DataField<>("instructions", new ArrayList<>());

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

    @NotNull
    public List<byte[]> getIngredients() {
        return this.ingredients.getValue();
    }

    public void setIngredients(@NotNull final List<byte[]> encodedIngredients) {
        this.ingredients.setValue(encodedIngredients);
    }

}
