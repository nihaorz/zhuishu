package com.nihaorz.zhuishu.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nihaorz.zhuishu.util.ZhuiShuAPI;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nihaorz
 */
@Controller
public class IndexAction {

    private static final String CHAPTERS_LIST_URL = "http://api.zhuishushenqi.com/atoc/577b5091a3d28cdb51244134?view=chapters";

    private static final String CHAPTERS_CONTENT_PREFIX = "/chapter/";

    private static final BASE64Encoder encoder = new BASE64Encoder();

    private static final BASE64Decoder decoder = new BASE64Decoder();

    private static final List<Map<String, String>> chapterList = new ArrayList<Map<String, String>>();

    private static final Map<String, String> infoMap = new HashMap<String, String>();

    static {
        infoMap.put("author", "妖夜");
        infoMap.put("title", "不灭龙帝");
        infoMap.put("url", "http://www.biquge.la/book/16064/");
    }

    @RequestMapping("/")
    public String index(ModelMap map) throws IOException {
        initAllChapters();
        map.putAll(infoMap);
        map.put("data", chapterList);
        return "index";
    }

    @RequestMapping("/chapter/{address}")
    public String chapter(@PathVariable("address") String address, ModelMap map) throws IOException {
        initAllChapters();
        map.putAll(infoMap);
        String url = new String(decoder.decodeBuffer(address));
        OkHttpClient client = new OkHttpClient();
        url = ZhuiShuAPI.CHAPTERS_CONTENT.replace(ZhuiShuAPI.REPLACE_KEY, url);
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String json = response.body().string();
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            map.put("chapterName", jsonObject.getAsJsonObject("chapter").get("title").getAsString());
            String content = jsonObject.getAsJsonObject("chapter").get("body").getAsString();
            List<String> list = Arrays.asList(content.split("\n"));
            List<String> result = new ArrayList<String>();
            int wordCount = 0;
            for (String s : list) {
                result.add("　　" + s);
                wordCount += s.length();
            }
            map.put("chapterBody", result);
            map.put("wordCount", wordCount);
        }

        for (int i = 0; i < chapterList.size(); i++) {
            if (chapterList.get(i).get("link").equals(CHAPTERS_CONTENT_PREFIX + address)) {
                if (i > 0) {
                    map.put("next", chapterList.get(i - 1));
                }
                if (i < chapterList.size() - 1) {
                    map.put("prev", chapterList.get(i + 1));
                }
            }
        }
        return "book";
    }

    private void initAllChapters() throws IOException {
        if (chapterList.size() == 0) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(CHAPTERS_LIST_URL).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                JsonArray chapters = jsonObject.getAsJsonArray("chapters");
                for (JsonElement chapter : chapters) {
                    Map<String, String> chapterMap = new HashMap<String, String>();
                    chapterMap.put("title", chapter.getAsJsonObject().get("title").getAsString());
                    chapterMap.put("link", CHAPTERS_CONTENT_PREFIX + encoder.encode(chapter.getAsJsonObject().get("link").getAsString().getBytes()));
                    chapterList.add(chapterMap);
                }
                Collections.reverse(chapterList);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

}
