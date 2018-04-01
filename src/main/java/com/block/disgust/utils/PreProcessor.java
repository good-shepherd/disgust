package com.block.disgust.utils;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

@Component
public class PreProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PreProcessor.class);
    private static final String YAGAL_URL = "http://gall.dcinside.com/board/lists/?id=baseball_new6&page=";
    private static final String POST_URL = "http://gall.dcinside.com/board/view/?id=baseball_new6&no=";

    // picList.get(0)[0]: filename, [1]: file ext, [2]: file url, [3] board no
    public List<String[]> getPicList(int latestbno) {
        List<String[]> picList = new ArrayList<>();
        List<Element> elements = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        map.put("Host", "gall.dcinside.com");
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "en-US,en;q=0.9,fr;q=0.8,it;q=0.7,ja;q=0.6,ko;q=0.5");
        map.put("Cache-Control", "max-age=0");
        map.put("Upgrade-Insecure-Requests", "1");
        for (int i = 1; i <= 5; i++) {
            Elements el = null;
            try {
                el = Jsoup.connect(YAGAL_URL + i).headers(map).get().select(".tb");
            } catch (SocketTimeoutException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Element[] tmp = new Element[el.size()];
            el.toArray(tmp);
            Collections.addAll(elements, tmp);
        }
        // logger.info("elapsed time for initial page loading: " + ((end - start) / 1000.0) + "sec");
        for (Element e : elements) {
            if (e.select("a").hasClass("icon_pic_n")) {
                String bno = e.select(".t_notice").text();
                if (Integer.parseInt(bno) > latestbno) {
                    String s = POST_URL.concat(bno);
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(s).headers(map).timeout(5000).get();
                    } catch (HttpStatusException e1) {
                        logger.info("the " + bno + "post has removed: --" + e1.getMessage() + "--");
                    } catch (SocketTimeoutException e1) {
                        logger.info(e1.getMessage() + ": " + bno);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    Elements tdelements = null;
                    try {
                        tdelements = doc.select(".icon_pic").select("a");
                    } catch (NullPointerException n) {
                        n.printStackTrace();
                        continue;
                    }

                /*System.out.println();
                logger.info("=========================================================================");
                logger.info("number of pics in a post: " + tdelements.size() + ", board number: " + bno);
                logger.info(tdelements.toString());
                int count = 0;*/
                    for (Element td : tdelements) {
                        String tmp = td.attr("href");
                        String f = tmp.substring(0, 26);
                        String r = tmp.substring(34);
                        String[] fileAndURL = new String[4];
                        System.arraycopy(splitFilename(td.text()), 0, fileAndURL, 0, 2);
                        fileAndURL[2] = f.concat("viewimage").concat(r);
                        fileAndURL[3] = bno;
                        picList.add(fileAndURL);
                        // count++;
                    }
                /*piclistcount += count;
                logger.info("loops : " + count);
                logger.info("piclist.size() v. piclistcount: " + picList.size() + " vs. " + piclistcount);
                logger.info("=========================================================================");
                System.out.println();*/
                }
            }
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
