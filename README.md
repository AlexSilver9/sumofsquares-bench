# Sum-of-Squares JMH Benchmark

A JMH (Java Microbenchmark Harness) project that compares a straightforward sum-of-squares implementation against a manually loop-unrolled variant over an `int[]`.

## Benchmarks

| Benchmark | Description |
|-----------|-------------|
| `baseline` | Iterates the array, calling a `square()` helper per element |
| `optimized` | Manual loop unrolling (stride 2) with inlined squaring and a cleanup loop for odd-length remainders |

Both benchmarks use `Blackhole.consume()` to prevent dead-code elimination by the JIT compiler.

### JMH Configuration

- **Mode:** `AverageTime` (lower is better)
- **Unit:** nanoseconds per operation
- **Warmup:** 5 iterations, 1 second each
- **Measurement:** 5 iterations, 1 second each
- **Forks:** 2
- **State:** per-thread, 1000-element array initialized once per trial

## Prerequisites

- **Java 17** or later
- **Maven 3.8+**

## Build

```bash
mvn clean package
```

This compiles the source, runs tests, and produces an executable shaded JAR via the `maven-shade-plugin` at:

```
target/sumofsquares-bench-1.0-SNAPSHOT.jar
```

## Run Benchmarks

Run all benchmarks:

```bash
java -jar target/sumofsquares-bench-1.0-SNAPSHOT.jar
```

Run a single benchmark by name:

```bash
java -jar target/sumofsquares-bench-1.0-SNAPSHOT.jar com.example.jmh.SumOfSquaresBenchmark.baseline
java -jar target/sumofsquares-bench-1.0-SNAPSHOT.jar com.example.jmh.SumOfSquaresBenchmark.optimized
```

## Project Structure

```
src/main/java/com/example/jmh/
    SumOfSquaresBenchmark.java   # Benchmark class with baseline and optimized methods
pom.xml                          # Maven build with JMH and shade plugin
```

## Results

On Win11 + WSL2 + Intel Core Ultra 9 285k + java-1.17.0-openjdk-amd64:

```
Benchmark                        Mode  Cnt    Score   Error  Units
SumOfSquaresBenchmark.baseline   avgt   10  242.932 ± 3.867  ns/op
SumOfSquaresBenchmark.optimized  avgt   10  145.398 ± 1.974  ns/op
```
