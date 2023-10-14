package sh.miles.aurorameals.data;

import org.jetbrains.annotations.NotNull;

public enum Measurement {

    TEASPOON("tsp"),
    TABLESPOON("Tbsp"),
    CUP("cup"),
    PINT("pt"),
    QUART("qt"),
    OUNCE("oz"),
    POUND("lb"),
    FLUID_OUNCES("fl oz"),
    PACKAGE("pkg"),
    BREAST("breast"),
    SPRAY("spray"),
    NONE("");

    private final String abrv;

    Measurement(@NotNull final String abrv) {
        this.abrv = abrv;
    }

    @Override
    public String toString() {
        return abrv;
    }
}
