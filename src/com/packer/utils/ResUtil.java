package com.packer.utils;

import java.net.URLEncoder;

/**
 * Created by yin on 2017/5/23.
 */
public class ResUtil {

    public static String getUrlEncode(String s) throws Exception {
        String urlEncode = "";
        urlEncode = URLEncoder.encode(s, "UTF-8");
        return urlEncode;
    }

}
