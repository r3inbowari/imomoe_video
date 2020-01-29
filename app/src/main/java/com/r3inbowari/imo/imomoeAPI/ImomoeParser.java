package com.r3inbowari.imo.imomoeAPI;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImomoeParser {
    public static final String P_VIDEO = "(',\\[)?'(?<num>.{0,8}?)\\$(?<url>.*?)\\$(?<type>.*?)'";

    public static ArrayList<ImomoeBangumiSource> parseVideosObject(final String jsCode) {
        // 去除源2
        int lastIndex = jsCode.lastIndexOf("],['");
        String js = "";
        if (lastIndex > 1) {
            js = jsCode.substring(0, lastIndex + 1);
        } else {
            js = jsCode;
        }
        // 匹配开始
        Pattern pattern = Pattern.compile(P_VIDEO);
        Matcher matcher = pattern.matcher(js);
        // Map<String, String> videoMap = new HashMap<>();
        // 改用array
        ArrayList<ImomoeBangumiSource> imomoeBangumiSources = new ArrayList<>();
//        while (matcher.find()) {
//            if (!videoMap.containsKey(matcher.group("num"))) {
//                videoMap.put(matcher.group("num"), matcher.group("url"));
//            }
//        }
        while (matcher.find()) {
//            String a = matcher.group(1);
//             a = matcher.group(2);
//             a = matcher.group(3);
//             a = matcher.group(4);
//             a = matcher.group(5);
// api26

            imomoeBangumiSources.add(new ImomoeBangumiSource(matcher.group(2), matcher.group(3)));
        }
        return imomoeBangumiSources;
    }
}
