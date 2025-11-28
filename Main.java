import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        System.out.println("========== PART 1: DEMO & PIPELINE ==========");
        runDemo();

        System.out.println("\n========== PART 2: UNIT TESTS (Manual) ==========");
        runTests();

        System.out.println("\n========== PART 3: PERFORMANCE TEST ==========");
        runPerformanceComparison();
    }

    public static void runDemo() {
        MyCustomInterface<String, Integer> strLen = s -> s.length();
        strLen.printResult("Hello Java");

        MyCustomInterface<Double, Long> rounder = Math::round;
        System.out.println("Rounded 5.6 -> " + rounder.execute(5.6));

        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 1200));
        products.add(new Product("Mouse", 20));
        products.add(new Product("Keyboard", 50));
        products.add(new Product("Monitor", 300));

        System.out.println("-- Products > 100$ --");
        Predicate<Product> expensiveFilter = p -> p.getPrice() > 100;
        Function<Product, String> nameMapper = Product::getName;
        Consumer<String> printer = name -> System.out.println("Item: " + name);

        products.stream()
                .filter(expensiveFilter)
                .map(nameMapper)
                .forEach(printer);

        Function<Integer, Integer> multiplyBy2 = x -> x * 2;
        Function<Integer, Integer> add10 = x -> x + 10;

        System.out.println("andThen result: " + multiplyBy2.andThen(add10).apply(5));
        System.out.println("compose result: " + multiplyBy2.compose(add10).apply(5));
    }

    public static void runTests() {
        int passed = 0;
        int failed = 0;

        MyCustomInterface<String, String> upper = String::toUpperCase;
        if ("TEST".equals(upper.execute("test"))) { passed++; } else { failed++; }

        MyCustomInterface<Integer, Integer> identity = MyCustomInterface.identity();
        if (identity.execute(50) == 50) { passed++; } else { failed++; }

        Function<Integer, Integer> x2 = x -> x * 2;
        Function<Integer, Integer> plus3 = x -> x + 3;
        
        if (x2.andThen(plus3).apply(2) == 7) { passed++; } else { failed++; }
        if (x2.compose(plus3).apply(2) == 10) { passed++; } else { failed++; }

        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        if (nums.stream().filter(n -> n % 2 == 0).count() == 2) { passed++; } else { failed++; }

        System.out.println("Tests: " + passed + " Passed, " + failed + " Failed.");
    }

    public static void runPerformanceComparison() {
        int size = 10_000;
        List<Integer> numbers = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) numbers.add(random.nextInt(1000));

        long startLoop = System.nanoTime();
        long sumLoop = 0;
        for (Integer num : numbers) {
            if (num % 2 == 0) sumLoop += (num * 2);
        }
        long endLoop = System.nanoTime();

        long startStream = System.nanoTime();
        long sumStream = numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToLong(n -> n * 2)
                .sum();
        long endStream = System.nanoTime();

        System.out.println("Loop Time:   " + (endLoop - startLoop) + " ns");
        System.out.println("Stream Time: " + (endStream - startStream) + " ns");
    }
}
