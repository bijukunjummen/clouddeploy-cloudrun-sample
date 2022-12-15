package org.bk.sample.web;

import org.bk.sample.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class LoadController {
    private AtomicBoolean running = new AtomicBoolean(false);

    private final ExecutorService executor;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadController.class);

    public LoadController() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Starting a threadpool with " + nThreads + " size");
        this.executor = Executors.newFixedThreadPool(nThreads);
    }

    @GetMapping("/load")
    public ResponseEntity<Message> sampleLoad(@RequestParam(name = "durationMillis", defaultValue = "5000") int duration) {
        if (!running.compareAndSet(false, true)) {
            return ResponseEntity.internalServerError().body(new Message("Could Not trigger cpu load..one running already"));
        }
        int triggerDuration = (duration > 15000) ? 15000 : duration;

        List<CompletableFuture<Integer>> futures = IntStream.range(1, Runtime.getRuntime().availableProcessors())
                .boxed()
                .map(n -> CompletableFuture.supplyAsync(new BusyThread(n, 0.5, triggerDuration), executor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, t) -> {
                    running.compareAndSet(true, false);
                });
        return ResponseEntity.accepted().body(new Message("triggered load for " + duration + " ms"));
    }

    private static class BusyThread implements Supplier<Integer> {

        private int proc;
        private double load;
        private long duration;

        /**
         * Constructor which creates the thread
         *
         * @param load     Load % that this thread should generate
         * @param duration Duration that this thread should generate the load for
         */

        public BusyThread(int proc, double load, long duration) {
            this.proc = proc;
            this.load = load;
            this.duration = duration;
        }

        @Override
        public Integer get() {
            long startTime = System.currentTimeMillis();
            // Loop for the given duration
            while (System.currentTimeMillis() - startTime < duration) {
                // Every 100ms, sleep for the percentage of unladen time
                if (System.currentTimeMillis() % 100 == 0) {
                    try {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            LOGGER.info("Completed proc: " + proc);
            return this.proc;
        }
    }
}
