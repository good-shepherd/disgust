package com.block.disgust.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /*@Scheduled(fixedRate = 3000)
    public void tasksWithFixedRate() {
        logger.info("fixed rate task !!!!!!:: execution time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }

    @Scheduled(fixedRate = 5000)
    public void tasksWithFixedRate2() {
        logger.info("fixed rate task !????????????:: execution time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }*/

}
