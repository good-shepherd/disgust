package com.block.disgust.utils;

import com.block.disgust.repositories.DisgustPicRepository;
import com.block.disgust.repositories.PicCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Crawler implements CommandLineRunner {

    private final PreProcessor preProcessor;
    private final Downloader downloader;
    private final DisgustPicRepository disgustPicRepository;
    private final PicCategoryRepository picCategoryRepository;

    @Autowired
    public Crawler(PreProcessor preProcessor, Downloader downloader, DisgustPicRepository disgustPicRepository, PicCategoryRepository picCategoryRepository) {
        this.preProcessor = preProcessor;
        this.downloader = downloader;
        this.disgustPicRepository = disgustPicRepository;
        this.picCategoryRepository = picCategoryRepository;
    }

    @Override
    public void run(String... args) {
        Long start = System.currentTimeMillis();
        final String path = "/Users/augustine/crawlertest/";
        downloader.download(preProcessor.getPicList(), path);
        Long end = System.currentTimeMillis();
        System.out.println("elapsed time: " + (end - start) / 1000.0);
        System.out.println("file download complete!");
    }
}
