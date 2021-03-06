package com.xinleju.platform.finance.utils;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class NCXMlParse {
	/**
	 * 以Connection方式发送XMl
	 * @param url
	 * @param xml
	 * @return
	 */
  public static String  sendxml(String url,String xml){
	    URL realURL;
	   // Logger logger = LoggerFactory.getLogger(FiVoucherCreateService.class);
		try {
			realURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)realURL.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Contect-type", "text/xml");
			connection.setRequestMethod("POST");
	        Writer writer =new OutputStreamWriter(connection.getOutputStream(),"utf-8");
	        IOUtils.write(xml, writer);
	        
	        InputStream inputStream = connection.getInputStream();
	        List<String> listString=IOUtils.readLines(inputStream);
	        StringBuffer sb=new StringBuffer();
	        for(int i=0;i<listString.size();i++){
	        	//byte[] by=listString.get(i).getBytes("utf-8");
	        	//String list=new String(listString.get(i).getBytes("gb2312"), "utf-8"); 
	        	sb.append(listString.get(i));
	        	//sb.append(list);
	        }
	        String result=sb.toString();
	      //  String result=IOUtils.toString(inputStream);
	        return result;
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.error("", e);
			return null;
		 }
		  
  }
  
     /**
     * 获取xml返回的错误信息 
     * @param xml
     * @return
     */
    public static String XmlErrorInfo(String xml){
    	 try {
			Document document = DocumentHelper.parseText(xml);
			Element root=document.getRootElement();
			Element sendresult=root.element("sendresult");
			if(sendresult!=null){
				Element resultdescription=sendresult.element("resultdescription");
				String result= resultdescription.getText();
				return result;
			}
		  } catch (Exception e) {
		 	// TODO Auto-generated catch block
			 e.printStackTrace();
			return null;
		  } 
    	 return null;
    	 
     }
    /**
     * 返回错误代码
     * @param xml
     * @return
     */
    public static int XmlErrorCode(String xml){
    	try{
    		Document document = DocumentHelper.parseText(xml);
			Element root=document.getRootElement();
			Element sendresult=root.element("sendresult");
			if(sendresult!=null){
				Element resultdescription=sendresult.element("resultcode");
				String result= resultdescription.getText();
				return Integer.parseInt(result);
			}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return -1;
    	}
    	return -1;
    }
    
    public static String XmlContent(String xml){
    	try{
    		Document document = DocumentHelper.parseText(xml);
			Element root=document.getRootElement();
			Element sendresult=root.element("sendresult");
			if(sendresult!=null){
				Element resultdescription=sendresult.element("content");
				String result= resultdescription.getText();
				return result;
			}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	return null;
    }
}
