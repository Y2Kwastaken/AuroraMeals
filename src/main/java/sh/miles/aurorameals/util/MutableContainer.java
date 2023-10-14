package sh.miles.aurorameals.util;

public class MutableContainer<T> {

    private T content;

    private MutableContainer(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public void set(T content) {
        this.content = content;
    }

    public static <T> MutableContainer<T> empty() {
        return new MutableContainer<>(null);
    }

    public static <T> MutableContainer<T> of(T content) {
        return new MutableContainer<>(content);
    }
}
