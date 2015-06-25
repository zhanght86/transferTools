package client.httpclient.common;

import client.bean.RegisterResult;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by QPing on 2015/4/8.
 */
public class FormPost {

    private static ObjectMapper mapper = new ObjectMapper() ;


    public static Object send(String ip, String port, String url,  List<NameValuePair> nvps, Class<?> clazz){
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://" + ip + ":" + port + url);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String retStr = EntityUtils.toString(entity, "UTF-8");

            if (StringUtils.isBlank(retStr)) {
                return null;
            }

            Object result = mapper.readValue(retStr, clazz);

            EntityUtils.consume(entity);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    /**
     * 发送一个http消息
     * @param ip            IP地址或者域名，如 10.20.17.214
     * @param port          端口，如 80
     * @param url           连接地址，如 /servelt/regServelt?methold=test
     * @param valuesPair    表单数据
     * @return
     */
    public static Result send(String ip, String port, String url, HashMap<String, String> valuesPair){
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Iterator iterator = valuesPair.keySet().iterator();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            String value = valuesPair.get(key);
            nvps.add(new BasicNameValuePair(key, value));
        }

        return (Result) send(ip, port, url, nvps, Result.class);
    }
}
