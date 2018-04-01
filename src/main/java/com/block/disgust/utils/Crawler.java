package com.block.disgust.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Crawler /*implements CommandLineRunner*/ {

    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private static int latestbno = 0;
    private final PreProcessor preProcessor;
    private final Downloader downloader;

    @Autowired
    public Crawler(PreProcessor preProcessor, Downloader downloader) {
        this.preProcessor = preProcessor;
        this.downloader = downloader;
    }

    /*@Override
    public void run(String... args) {
        Long start = System.currentTimeMillis();
        final String path = "/Users/augustine/crawlertest/";
        List<String[]> picList = preProcessor.getPicList();
        Collections.sort(picList, new ArrayComparator());
        String latestbno = picList.get(0)[3];
        for (int i = 0; i < picList.size(); i++) {
            System.out.printf("index: %d, bno: %s, name: %s\n", i, picList.get(i)[3], picList.get(i)[0]);
        }
        // downloader.download(picList, path);
        Long end = System.currentTimeMillis();
        logger.info("elapsed time: " + (end - start) / 1000.0 + "sec");
        logger.info("file download complete!");
    }*/

    @Scheduled(fixedDelay = 30000)
    public void downloadNewPics() {
        Long start = System.currentTimeMillis();
        final String path = "/Users/augustine/crawlertest/";
        List<String[]> picList = preProcessor.getPicList(latestbno);
        Collections.sort(picList, new ArrayComparator());
        latestbno = Integer.parseInt(picList.get(0)[3]);
        /*for (int i = 0; i < picList.size(); i++) {
            System.out.printf("index: %d, bno: %s, name: %s\n", i, picList.get(i)[3], picList.get(i)[0]);
        }*/
        downloader.download(picList, path);
        Long end = System.currentTimeMillis();
        logger.info("elapsed time: " + (end - start) / 1000.0 + "sec");
        logger.info("latestbno: " + latestbno);
        logger.info("file download complete!");

    }
}
