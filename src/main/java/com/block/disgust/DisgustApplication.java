package com.block.disgust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DisgustApplication {
    public static void main(String[] args) {
        SpringApplication.run(DisgustApplication.class, args);
    }
}
