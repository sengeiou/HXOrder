package com.happysnaker.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/2
 * @email happysnaker@foxmail.com
 */
public class JsonUtils {
    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

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

}
