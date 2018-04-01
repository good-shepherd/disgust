package com.block.disgust.utils;

import com.block.disgust.entities.DisgustPic;
import com.block.disgust.repositories.DisgustPicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
public class Downloader {

    private static final Logger logger = LoggerFactory.getLogger(Downloader.class);
    private final DisgustPicRepository disgustPicRepository;
    // private final PicCategoryRepository picCategoryRepository;

    @Autowired
    public Downloader(DisgustPicRepository disgustPicRepository/*, PicCategoryRepository picCategoryRepository*/) {
        this.disgustPicRepository = disgustPicRepository;
        // this.picCategoryRepository = picCategoryRepository;
    }

    public int download(List<String[]> picList, String path) {
        int i;
        new File(path).mkdirs();
        for (i = 0; i < picList.size(); i++) {
            try {
                String[] dp = picList.get(i);
                URL url = new URL(dp[2]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 1048576);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path.concat("/").concat(dp[0]).concat(".").concat(dp[1])), 1048576);
                int ch;
                while ((ch = bis.read()) != -1) {
                    bos.write(ch);
                }
                bis.close();
                bos.close();
                DisgustPic dis = new DisgustPic(dp[0].concat(".").concat(dp[1]), path, Integer.parseInt(dp[3]));
                disgustPicRepository.save(dis);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("downloaded file: {}", ("No." + (i + 1) + " - " + picList.get(i)[0] + "." + picList.get(i)[1]));
        }
        return i;
    }
}
