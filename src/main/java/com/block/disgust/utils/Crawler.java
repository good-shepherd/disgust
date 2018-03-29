package com.block.disgust.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Crawler implements CommandLineRunner {

    private final PreProcessor preProcessor;
    private final Downloader downloader;

    @Autowired
    public Crawler(PreProcessor preProcessor, Downloader downloader) {
        this.preProcessor = preProcessor;
        this.downloader = downloader;
    }

    @Override
    public void run(String... args) {
        Long start = System.currentTimeMillis();
        final String path = "/Users/augustine/crawlertest/";
        List<String[]> picList = preProcessor.getPicList();
        /*for (String[] strings : picList) {
            System.out.printf("name: %s, ext: %s, url: %s, bno: %s \n", strings[0], strings[1], strings[2], strings[3]);
        }*/
        downloader.download(picList, path);
        Long end = System.currentTimeMillis();
        System.out.println("elapsed time: " + (end - start) / 1000.0);
        System.out.println("file download complete!");
    }
}
