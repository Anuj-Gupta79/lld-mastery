package com.lld.problems.lrucache.code;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.problems.lrucache.code.core.LRUCache;

public class Main {

    public static void main(String[] args) throws Exception {
        scenario1();
        scenario2();
        scenario3();
        scenario4();
        scenario5();
        scenario6();
        scenario7();
    }

    private static void show(String label, Object result) {
        System.out.println("  -> " + label + " = " + result);
    }

    private static void expectError(String label, Runnable action) {
        try {
            action.run();
            show(label, "no exception");
        } catch (RuntimeException e) {
            show(label, e.getClass().getSimpleName());
        }
    }

    // capacity 2: read refreshes a key
    private static void scenario1() {
        System.out.println("Scenario 1: get refreshes a key");
        LRUCache<String, Integer> c = new LRUCache<>(2);
        c.put("A", 1);
        c.put("B", 2);
        show("get A", c.get("A"));
        c.put("C", 3);
        show("get B", c.get("B"));
        show("get A", c.get("A"));
        show("get C", c.get("C"));
    }

    // capacity 2: put on an existing key updates and refreshes
    private static void scenario2() {
        System.out.println("Scenario 2: put on existing key");
        LRUCache<String, Integer> c = new LRUCache<>(2);
        c.put("A", 1);
        c.put("B", 2);
        c.put("A", 9);
        c.put("C", 3);
        show("get A", c.get("A"));
        show("get B", c.get("B"));
        show("get C", c.get("C"));
    }

    // capacity 1: every put evicts
    private static void scenario3() {
        System.out.println("Scenario 3: capacity 1");
        LRUCache<String, Integer> c = new LRUCache<>(1);
        c.put("A", 1);
        c.put("B", 2);
        c.put("C", 3);
        show("get A", c.get("A"));
        show("get B", c.get("B"));
        show("get C", c.get("C"));
    }

    // capacity 2: remove frees a slot
    private static void scenario4() {
        System.out.println("Scenario 4: remove");
        LRUCache<String, Integer> c = new LRUCache<>(2);
        c.put("A", 1);
        c.put("B", 2);
        show("remove A", c.remove("A"));
        show("remove A again", c.remove("A"));
        c.put("C", 3);
        show("get B", c.get("B"));
        show("get C", c.get("C"));
        c.put("D", 4);
        show("get B", c.get("B"));
        show("get C", c.get("C"));
        show("get D", c.get("D"));
    }

    // invalid input
    private static void scenario5() {
        System.out.println("Scenario 5: invalid input");
        LRUCache<String, Integer> c = new LRUCache<>(2);
        expectError("capacity 0", () -> new LRUCache<String, Integer>(0));
        expectError("capacity -3", () -> new LRUCache<String, Integer>(-3));
        expectError("get null", () -> c.get(null));
        expectError("put null", () -> c.put(null, 1));
        expectError("remove null", () -> c.remove(null));
    }

    // 8 threads write different keys, then every key must be there
    private static void scenario6() throws Exception {
        System.out.println("Scenario 6: concurrent writes, no lost entries");
        int threads = 8;
        int perThread = 100;
        LRUCache<String, Integer> c = new LRUCache<>(threads * perThread);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<?>> futures = new ArrayList<>();

        for (int t = 0; t < threads; t++) {
            final int id = t;
            futures.add(pool.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    c.put("t" + id + "-k" + i, id * 1000 + i);
                }
            }));
        }
        for (Future<?> f : futures) {
            f.get();
        }
        pool.shutdown();

        int wrong = 0;
        for (int t = 0; t < threads; t++) {
            for (int i = 0; i < perThread; i++) {
                Integer v = c.get("t" + t + "-k" + i);
                if (v == null || v != t * 1000 + i) {
                    wrong++;
                }
            }
        }
        show("wrong or missing values", wrong);
    }

    // small capacity, many threads, mixed operations
    private static void scenario7() throws Exception {
        System.out.println("Scenario 7: concurrent mixed operations");
        LRUCache<String, Integer> c = new LRUCache<>(50);
        AtomicInteger errors = new AtomicInteger();
        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<?>> futures = new ArrayList<>();

        for (int t = 0; t < threads; t++) {
            futures.add(pool.submit(() -> {
                try {
                    for (int i = 0; i < 5000; i++) {
                        String key = "k" + ThreadLocalRandom.current().nextInt(200);
                        int op = ThreadLocalRandom.current().nextInt(3);
                        if (op == 0) {
                            c.put(key, i);
                        } else if (op == 1) {
                            c.get(key);
                        } else {
                            c.remove(key);
                        }
                    }
                } catch (RuntimeException e) {
                    errors.incrementAndGet();
                }
            }));
        }
        for (Future<?> f : futures) {
            f.get();
        }
        pool.shutdown();

        show("threads that hit an exception", errors.get());
        c.put("final", 1);
        show("get final", c.get("final"));
    }
}