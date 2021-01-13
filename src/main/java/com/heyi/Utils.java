package com.heyi;

import java.io.File;
import java.io.FileInputStream;

/**
 * 2017/08/16.
 */
public class Utils {

    /**
     * 关键字过滤
     */
    public static Boolean isKeyword(String filename) {
        if ("type".equals(filename) || "rename".equals(filename) || "delete".equals(filename) || "iterate".equals(filename)) {
            return true;
        }
        return false;
    }

    /**
     * 下划线命名转为驼峰命名
     *
     * @param str 下划线命名格式
     * @return 驼峰命名格式
     */
    public static String toCamel(String str) {
        if (!str.contains("_")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean flag = false;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '_') {
                flag = true;
            } else {
                if (flag) {
                    sb.append(Character.toUpperCase(ch));
                    flag = false;
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串的第一位转为小写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toLowerFirst(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    /**
     * 将字符串的第一位转为大写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toUpperFirst(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    /**
     * 转为小写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toLower(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }
        return new String(chars);
    }


}
