package com.block.disgust.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class Crawler {

    @Value("${app.filepath}")
    private String filePath;

    private static int latestbno = 0;
    private final PreProcessor preProcessor;
    private final Downloader downloader;

    public Crawler(PreProcessor preProcessor, Downloader downloader) {
        this.preProcessor = preProcessor;
        this.downloader = downloader;
    }

    @Scheduled(fixedDelay = 30000)
    public void downloadNewPics() {
        Long start = System.currentTimeMillis();
        int tmp = latestbno;
        List<String[]> picList = preProcessor.getPicList(latestbno);
        Collections.sort(picList, new ArrayComparator());
        latestbno = Integer.parseInt(picList.get(0)[3]);
        /*for (int i = 0; i < picList.size(); i++) {
        System.out.printf("index: %d, bno: %s, name: %s\n", i, picList.get(i)[3], picList.get(i)[0]);
        }*/
        downloader.download(picList, filePath);
        Long end = System.currentTimeMillis();
        log.info("elapsed time: " + (end - start) / 1000.0 + "sec");
        log.info("file download complete: started with " + tmp + " - ended with " + latestbno);
    }
}
