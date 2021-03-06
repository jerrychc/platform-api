package com.xinleju.platform.utils;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * Created by luoro on 2017/6/11.
 */
public class HttpClientUtil {
    private String url;
    private Map<String,String> map;
    private String charset;
    private static String phone;
    private static String content;

    public HttpClientUtil(String url, Map<String, String> map, String charset,String phone,String content) {
        this.url = url;
        this.map = map;
        this.charset = charset;
        this.phone = phone;
        this.content = content;
    }

    public MessageResult doPost(){
        MessageResult result = new MessageResult();
        CloseableHttpResponse response=null;
        try{
            CloseableHttpClient httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
             httpPost.setHeader("Content-Type","application/json");
             httpPost.setHeader ("token","xyre");
             httpPost.setEntity (new StringEntity (JacksonUtils.toJson (map),"UTF-8"));
             System.out.println("+++++同步数据+++++++"+JacksonUtils.toJson (map)+"+++++++++++++++");
             System.out.println("++++++++++++++++"+content+"++++++++++++++++");
             response = httpClient.execute(httpPost);
             result.setSuccess (true);
           /* if(response != null){
                if (!HttpCheckUtils.checkStatus(response.getStatusLine().getStatusCode())) {
//                    EntityUtils.consumeQuietly(response.getEntity());
                }
                HttpEntity resEntity = response.getEntity ();
                if (resEntity != null) {
                   result = JacksonUtils.fromJson (EntityUtils.toString (resEntity, "UTF-8"), MessageResult.class);
                    System.out.println(result);
                }
            }*/

        }catch(Exception ex){
            ex.printStackTrace ();
            result.setSuccess(false);
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
        }finally {
            if(response!=null){
                try {
                    EntityUtils.consumeQuietly(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static CloseableHttpClient getHttpClient() {
        ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new ServiceUnavailableRetryStrategy() {
            /**
             * retry逻辑
             */
            @Override
            public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                if (executionCount > 3) {
                    return false;
                }
                if (response != null) {
                    if (!HttpCheckUtils.checkStatus (response.getStatusLine ().getStatusCode ())) {
//                        EntityUtils.consumeQuietly (response.getEntity ());
                        return true;
                    }
                    HttpEntity resEntity = response.getEntity ();
                    if (resEntity != null) {
                        try {
                            MessageResult result = JacksonUtils.fromJson (EntityUtils.toString (resEntity, "UTF-8"), MessageResult.class);
                            if (!result.getCode ().equals (SyncErrorInfo.OK.getCode ())) {
                                return true;
                            }
                        } catch (IOException e) {

                        }

                    }
                }
                    return false;

            }
            /**
             * retry间隔时间
             */
            @Override
            public long getRetryInterval() {
                return 5000;
            }
        };
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(256);
        cm.setDefaultMaxPerRoute(128);
        CloseableHttpClient httpClient = HttpClients.custom().setUserAgent("XLJ-ERP HTTP Client").setServiceUnavailableRetryStrategy (serviceUnavailableRetryStrategy).setRetryHandler(new StandardHttpRequestRetryHandler (3, true))
                .setConnectionManager(cm).build();
        return httpClient;
    }
}

