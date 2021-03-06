package com.xinleju.platform.base.filter;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import com.rabbitmq.http.client.domain.UserInfo;

public class HttpForwardProxy {
	
	private static Logger log = Logger.getLogger(HttpForwardProxy.class);
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url, HttpCookie[] cookies) {
		
		String html = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			
			if(cookies != null && cookies.length > 0) {
				for (HttpCookie httpCookie : cookies) {
					httpget.addHeader("Cookie", httpCookie.getName() + "=" + httpCookie.getValue());
				}
			}
			
			log.info("executing request " + httpget.getURI());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				log.info("--------------------------------------");
				// 打印响应状态
				log.info(response.getStatusLine());
				
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//200正常  其他就不对  
					// 获取响应实体
					HttpEntity entity = response.getEntity();
					
					//获得相应实体  
	                if (entity!=null) {  
	                	
	                	Header[] headers = response.getAllHeaders();
	                	
	                	log.info("headers : " + Arrays.toString(headers));
	                	
	                    html = EntityUtils.toString(entity);//获得html源代码 
	                    // 打印响应内容长度
						log.info("Response content length: "+ entity.getContentLength());
						// 打印响应内容
						log.debug("Response content: "+ html);
	                }  
	            }  
				log.info("------------------------------------");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return html;
	}
	
public CASLoginResult casLogin(String url, List<NameValuePair> formParams) {
		
		CASLoginResult result = null;
		
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列    
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            log.info("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);
            
            log.info("headers : " + Arrays.toString(response.getAllHeaders()));
            
            log.info(response.getStatusLine());
            
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            	Header locationHeader = response.getFirstHeader("location");
            	String redirectUrl = locationHeader.getValue();
            	
            	List<HttpCookie> cookieList = new ArrayList<HttpCookie>();
            	
            	for (Header h : response.getAllHeaders()) {
					if("Set-Cookie".equals(h.getName()) && h.getValue() != null && h.getValue().trim().length() > 0 && h.getValue().trim().startsWith("CASTGC")) {
						String[] cookies = h.getValue().split(";")[0].split("=");
						if(cookies.length == 2) {
							cookieList.add(new HttpCookie(cookies[0], cookies[1]));
						}
					}
				}
            	result = new CASLoginResult(redirectUrl, cookieList.toArray(new HttpCookie[0]));
                
            	log.info("redirectUrl: " + redirectUrl);  
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	//200正常  其他就不对  
            	try {  
            		HttpEntity entity = response.getEntity(); 
            		
            		if (entity != null) {  
            			log.debug("--------------------------------------");  
            			log.debug("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
            			log.debug("--------------------------------------");  
            		}  
            	} finally {  
            		response.close();  
            	}  
            }
				
        } catch (Exception e) {  
            log.error(e.getMessage(), e);
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
            	log.error(e.getMessage(), e);
            }  
        }
        
        return result;
    }
	

	


}
