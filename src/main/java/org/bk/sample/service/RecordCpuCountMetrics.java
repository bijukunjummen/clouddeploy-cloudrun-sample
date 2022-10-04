package org.bk.sample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RecordCpuCountMetrics implements ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordCpuCountMetrics.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        LOGGER.info("Cpu Count for event: {}, is: {}",
                event.getClass().getName(), Runtime.getRuntime().availableProcessors());
    }
}
