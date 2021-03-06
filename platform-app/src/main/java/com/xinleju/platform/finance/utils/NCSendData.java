package com.xinleju.platform.finance.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class NCSendData {
	public static String getPostResponse(String url,String xml) {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);  
        // 创建参数队列    
        StringEntity entity1 = null;
       
        try {  
            entity1=new StringEntity(xml,"gb2312");
            httppost.setHeader("Content-Type", "text/xml;charset=utf-8");
            httppost.setEntity(entity1);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                	//System.out.println(EntityUtils.toString(entity, "UTF-8"));
                return EntityUtils.toString(entity, "UTF-8");
                
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
            	
                httpclient.close();  
                
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
		return null;  
	}
}