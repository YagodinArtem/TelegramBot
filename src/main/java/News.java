package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class News {

    /*
     * http://newsapi.org/v2/top-headlines?country=ru&apiKey=
     * 86627f6339294c5fa2086580a09f90d1
     *
     * token ^ news
     *
     *
     *
     */

    private static final int newsQuantity = 7;

    public static int getNewsQuantity() {
        return newsQuantity;
    }

    public static List<String> getNews() {
        List<String> news = new ArrayList<>();
        try {
            URL url = new URL("http://newsapi.org/v2/top-headlines?country=ru&apiKey=86627f6339294c5fa2086580a09f90d1");
            Scanner sc = new Scanner((InputStream) url.getContent());
            String result = "";
            while (sc.hasNext()) {
                result += sc.nextLine();
            }

            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("articles");

            for (int i = 0; i < newsQuantity; i++) {
                String res = "";
                JSONObject j = array.getJSONObject(i);
                //j.get("title") + "\n"
                res += j.get("url");
                news.add(res);
            }


            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
}
