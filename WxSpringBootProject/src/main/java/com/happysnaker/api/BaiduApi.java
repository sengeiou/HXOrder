package com.happysnaker.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * @author Happysnaker
 * @description
 * @date 2021/11/6
 * @email happysnaker@foxmail.com
 */
public class BaiduApi {
    private final static String AK = "aDIa4rKKUfyFsqW32D7GqZEweokzDMGP";
    private final static String URL = "http://api.map.baidu.com/geocoding/v3/?output=json&location=showLocation";

    public static String[] getLatitudeAndLongitude(String address) {
        String url = URL + "&ak=" + AK + "&address=" + address;
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        String[] addrs = new String[2];
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            int lngBegin = html.indexOf("\"lng\":") + 6;
            int lngEnd = html.indexOf(",\"lat\"");
            addrs[0] = html.substring(lngBegin, lngEnd);

            String latStr= html.substring(lngEnd + 7);
            int latEnd = latStr.indexOf("}");
            addrs[1] = latStr.substring(0, latEnd);
            return addrs;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
