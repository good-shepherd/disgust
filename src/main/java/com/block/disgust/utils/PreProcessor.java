package com.block.disgust.utils;

import org.jsoup.HttpStatusException;
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

    // picList.get(0)[0]: filename, [1]: file ext, [2]: file url, [3] board no
    public List<String[]> getPicList() {
        List<String[]> picList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(YAGAL_URL).get();
            Elements tb = doc.select(".tb");
            Element[] elements = new Element[tb.size()];
            tb.toArray(elements);
            for (Element e : elements) {
                if (e.select("a").hasClass("icon_pic_n")) {
                    String bno = e.select(".t_notice").text();
                    String s = POST_URL.concat(bno);
                    doc = Jsoup.connect(s).get();
                    Elements tdelements = doc.select("ul").select(".appending_file").select("a");
                    for (Element e1 : tdelements) {
                        String tmp = e1.attr("href");
                        String f = tmp.substring(0, 26);
                        String r = tmp.substring(34);
                        String[] fileAndURL = new String[4];
                        System.arraycopy(splitFilename(e1.text()), 0, fileAndURL, 0, 2);
                        fileAndURL[2] = f.concat("viewimage").concat(r);
                        fileAndURL[3] = bno;
                        picList.add(fileAndURL);
                    }
                }
            }
        } catch (HttpStatusException e) {
            System.out.println("the post has removed: --" + e.getMessage() + "--");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picList;
    }

    private String[] splitFilename(String filename) {
        String[] result = new String[2];
        int i = filename.lastIndexOf('.');
        result[0] = filename.substring(0, i);
        result[1] = filename.substring(i + 1);
        return result;
    }
}
