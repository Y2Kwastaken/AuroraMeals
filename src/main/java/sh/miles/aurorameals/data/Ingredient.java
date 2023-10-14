package sh.miles.aurorameals.data;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Ingredient implements Serializable {

    @Serial
    private static final long serialVersionUID = 3818581520334446208L;

    private String name;
    private byte[] amount;
    private Measurement measurement;

    public Ingredient(@NotNull final String name, final byte @NotNull [] amount, @NotNull final Measurement measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public byte[] getAmount() {
        return amount;
    }

    public String getAmountAsFraction() {
        if (amount[1] == 1 || amount[1] == 0) {
            return String.valueOf(amount[0]);
        }
        return amount[0] + "/" + amount[1];
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(byte[] amount) {
        this.amount = amount;
    }

    public void setAmountAsFraction(@NotNull final String fraction) {
        final String[] splitFraction = fraction.split("/");
        amount[0] = Byte.parseByte(splitFraction[0]);
        amount[1] = Byte.parseByte(splitFraction[1]);
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public byte[] serialize() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(this);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Ingredient that)) return false;
        return Objects.equals(name, that.name) && Arrays.equals(amount, that.amount) && measurement == that.measurement;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, measurement);
        result = 31 * result + Arrays.hashCode(amount);
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient{" + "name='" + name + '\'' + ", amount=" + Arrays.toString(amount) + ", measurement=" + measurement + '}';
    }

    public static Ingredient deserialize(final byte @NotNull [] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (Ingredient) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
