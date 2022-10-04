package org.bk.sample.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("gcp")
public class RecordCpuCountMetrics {
    public static final String CPU_COUNT = "cpu.count";
    private final MeterRegistry meterRegistry;

    public RecordCpuCountMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 5000)
    public void recordCpuCount() {
        meterRegistry.gauge(CPU_COUNT, Runtime.getRuntime().availableProcessors());
    }
}
