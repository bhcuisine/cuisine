package com.bhsoftware.projectserver.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/6
 */
public class StringUtils {

    public static String getRandomString(int length) {

        String base = "abcefghigklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
