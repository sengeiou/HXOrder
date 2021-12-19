package com.happysnaker.utils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/26
 * @email happysnaker@foxmail.com
 */
public class MyBatisUtils {
    public static String generateResultMap(Class c) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String result = "<result column=\"" + name.replaceAll("[A-Z]", "_$0").toLowerCase() + "\" property=\"" + name + "\"></result>";
            sb.append(result).append('\n');
        }
        return sb.toString();
    }

    public static String generateInsertSql(Class c) {
        Field[] fields = c.getDeclaredFields();
        String table = c.getSimpleName().replaceAll("[A-Z]", "_$0").toLowerCase();
        if (table.charAt(0) == '_') {
            table = table.replaceFirst("_", "");
        }
        StringBuilder sb = new StringBuilder("INSERT INTO `" + table + "`(");
        for (Field field : fields) {
            String name = field.getName().replaceAll("[A-Z]", "_$0").toLowerCase();
            sb.append("`" + name + "`,");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(") SELECT ");
        for (Field field : fields) {
            String name = field.getName();
            sb.append("#{" + name + "},");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
