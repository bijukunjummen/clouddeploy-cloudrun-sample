package org.bk.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        System.out.println(
                String.format("Cpu Count before startup: %d",
                        Runtime.getRuntime().availableProcessors()));
        SpringApplication.run(Application.class, args);
    }
}
