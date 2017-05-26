package com.packer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yin on 2017/5/23.
 */
public class ResUtil {

    public static String getUrlEncode(String s) throws Exception {
        String urlEncode = "";
        urlEncode = URLEncoder.encode(s, "UTF-8");
        return urlEncode;
    }

    /**
     * 格式化json
     *
     * @param content
     * @return
     */
    public static String formatJson(String content) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(content);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

}
