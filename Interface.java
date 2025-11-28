@FunctionalInterface
public interface MyCustomInterface<T, R> {
    R execute(T t);

    default void printResult(T t) {
        System.out.println("Default Method Output: " + execute(t));
    }

    static <T> MyCustomInterface<T, T> identity() {
        return t -> t;
    }
}
