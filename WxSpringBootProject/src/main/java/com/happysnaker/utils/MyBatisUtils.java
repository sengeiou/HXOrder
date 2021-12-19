package com.happysnaker.utils;

import java.lang.reflect.Field;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/26
 * @email happysnaker@foxmail.com
 */
public class MyBatisUtils {
    public static String makeResultMap(Class c) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();

            String result = "<result column=\"" + name.replaceAll("[A-Z]", "_$0").toLowerCase() + "\" property=\"" + name + "\"></result>";
            sb.append(result).append('\n');
        }
        return sb.toString();
    }
}
