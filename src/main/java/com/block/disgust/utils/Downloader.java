package com.block.disgust.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
public class Downloader {

    public int download(List<String[]> picList, String path) {
        int i = 0;
        new File(path).mkdirs();
        for (i = 0; i < picList.size(); i++) {
            try {
                URL url = new URL(picList.get(i)[2]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 1048576);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path.concat("/").concat(picList.get(i)[0]).concat(".").concat(picList.get(i)[1])), 1048576);
                int ch;
                while ((ch = bis.read()) != -1) {
                    bos.write(ch);
                }
                bis.close();
                bos.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No." + (i + 1) + ": " + picList.get(i)[0] + "." + picList.get(i)[1]);
        }
        return i;
    }
}
