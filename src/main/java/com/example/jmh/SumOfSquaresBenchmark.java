package com.example.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@State(Scope.Thread)
public class SumOfSquaresBenchmark {

    private int[] values;

    @Setup(Level.Trial)
    public void setup() {
        values = new int[1_000];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }
    }

    // ---------------------------
    // Baseline version
    // ---------------------------
    @Benchmark
    public void baseline(Blackhole bh) {
        bh.consume(sumOfSquares(values));
    }

    private static int square(int x) {
        return x * x;
    }

    private static int sumOfSquares(int[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += square(values[i]);
        }
        return sum;
    }

    // ---------------------------
    // "Optimized" version
    // ---------------------------
    @Benchmark
    public void optimized(Blackhole bh) {
        bh.consume(sumOfSquaresOptimized(values));
    }

    private static int sumOfSquaresOptimized(int[] values) {
        int sum = 0;
        int i = 0;
        int len = values.length;

        for (; i <= len - 2; i += 2) {
            int v1 = values[i];
            int v2 = values[i + 1];
            sum += v1 * v1 + v2 * v2;
        }

        for (; i < len; i++) {
            int v = values[i];
            sum += v * v;
        }

        return sum;
    }
}

