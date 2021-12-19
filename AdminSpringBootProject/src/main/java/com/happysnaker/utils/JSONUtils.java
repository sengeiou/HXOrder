package com.happysnaker.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 封装输出JSON格式的类
 */
public class JSONUtils {
    public static JSONObject getJSONObject(Object obj) {
        JSONObject object = new JSONObject();
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
                continue;
            }
            try {
                field.setAccessible(true);
                object.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static JSONObject listAddToJsonObject(JSONObject jsonObject, List list) {
        return listAddToJsonObject(jsonObject, list, "arrays");
    }

    public static JSONObject listAddToJsonObject(JSONObject jsonObject, List list, String key) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONArray jsonArray = new JSONArray();
        for (Object obj : list) {
            if (obj != null) {
                jsonArray.add(obj);
            }
        }
        jsonObject.put(key, jsonArray);
        return jsonObject;
    }


    /**
     * 输出JSON
     * @param response
     * @param data
     * @throws IOException
     * @throws ServletException
     */
    public static void writeJSON(
                         HttpServletResponse response,
                         Object data) throws IOException, ServletException {
        //这里很重要，否则页面获取不到正常的JSON数据集
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Method", "POST,GET");
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(data));
        out.flush();
        out.close();
    }
}
