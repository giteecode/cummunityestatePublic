package com.project.property.utils;

import java.util.List;

/**
 * 处理字符串的工具类
 */
public class StringUtils {

    /**
     * 将传入的List分解成指定字符拼接的字符串, 例如：将 [1,2,3,4] 分解为 1,2,3,4
     * @param list
     * @param symbol
     * @return
     */
    public static String untieListReturnString(List<Object> list, String symbol) {
        if(list == null || list.size() == 0 || symbol == null || "".equals(symbol)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)+symbol);
        }
        return sb.substring(0, sb.length()-1);
    }

    /**
     * 判断传入的字符串是否为空
     * ""       返回true
     * NULL     返回true
     * "NULL"   返回true
     * " "      返回true
     * @param s
     * @return
     */
    public static boolean isNull(String s) {
        if(s == null || "".equals(s.trim()) || s.trim().length() == 0 || "null".equalsIgnoreCase(s)) {
            return true;
        }
        return false;
    }
}
