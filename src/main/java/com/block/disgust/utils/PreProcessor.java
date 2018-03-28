package com.block.disgust.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PreProcessor {

    private static final String YAGAL_URL = "http://gall.dcinside.com/board/lists/?id=baseball_new6";
    private static final String POST_URL = "http://gall.dcinside.com/board/view/?id=baseball_new6&no=";

    public List<String[]> getPicList(){
        List<String[]> picList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(YAGAL_URL).get();
            Elements tb = doc.select(".tb");
            Element[] elements = new Element[tb.size()];
            tb.toArray(elements);
            List<String> post = new ArrayList<>();
            for (Element e : elements) {
                if (e.select("a").hasClass("icon_pic_n")) {
                    post.add(POST_URL.concat(e.select(".t_notice").text()));
                }
            }
            for (String s : post) {
                doc = Jsoup.connect(s).get();
                Elements tdelements = doc.select("ul").select(".appending_file").select("a");
                for (Element e : tdelements) {
                    String tmp = e.attr("href");
                    String f = tmp.substring(0, 26);
                    String r = tmp.substring(34);
                    String fileName = e.text();
                    String[] fileAndURL = new String[3];
                    System.arraycopy(fileName.split("\\.(?=[^\\.]+$)"), 0, fileAndURL, 0, 2);
                    fileAndURL[2] = f.concat("viewimage").concat(r);
                    picList.add(fileAndURL);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picList;
    }
}
