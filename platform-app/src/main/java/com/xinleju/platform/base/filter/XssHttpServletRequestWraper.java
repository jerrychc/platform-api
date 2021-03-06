package com.xinleju.platform.base.filter;

import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by luoro on 2017/7/9.
 */

public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {

    private Map<String , String[]> params = new HashMap<String, String[]> ();
    private byte[] buffer;
    public XssHttpServletRequestWraper(HttpServletRequest request)throws IOException {
        super (request);
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
       // 获取持有responseBodyd的ServletInputStream流
        InputStream is = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buff[] = new byte[ 1024 ];
        int read;
        while( ( read = is.read( buff ) ) > 0 ) {
            baos.write( buff, 0, read );
        }
        this.buffer = baos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
       BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) super.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
       Map<String,Object> requestBody = JacksonUtils.fromJson (new String(this.buffer,"UTF-8"),Map.class);
        Set<String> requestBodyKeys = requestBody.keySet ();
        for(String key:requestBodyKeys){
            if(requestBody.get (key) instanceof String){
                if(Objects.equals (key,"groupByFields")
                        ||Objects.equals (key,"sortFields")
                        ||Objects.equals (key,"fuzzyQueryFields")){
                       continue;
                }
                requestBody.put (key,xssEncode ((String)requestBody.get (key)));
            }
        }
        String requestPayload = JacksonUtils.toJson (requestBody);
        return new BufferedServletInputStream( requestPayload.getBytes ("UTF-8"));
    }

    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return xssEncode(values[0]);
    }

    @Override
    public String getHeader(String name) {
        return xssEncode(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        if(!StringUtils.isEmpty (name)){
            String[] values = params.get(name);
            if(values != null && values.length > 0){
                String[] newValues = new String[values.length];

                for(int i =0; i< values.length; i++){
                    newValues[i] = xssEncode(values[i]);
                }
                return newValues;
            }
        }
        return null;
    }

    /**
     * 将特殊字符替换为全角
     * @param s
     * @return
     */
    private  String xssEncode(String s) {

       /* if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');// 全角大于号
                    break;
                case '<':
                    sb.append('＜');// 全角小于号
                    break;
                case '\'':
                    sb.append('‘');// 全角单引号
                    break;
                case '\"':
                    sb.append('“');// 全角双引号
                    break;
                case '&':
                    sb.append('＆');// 全角＆
                    break;
                case '\\':
                    sb.append('＼');// 全角斜线
                    break;
                case '/':
                    sb.append('／');// 全角斜线
                    break;
                case '#':
                    sb.append('＃');// 全角井号
                    break;
                case '(':
                    sb.append('（');// 全角(号
                    break;
                case ')':
                    sb.append('）');// 全角)号
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();*/
       return  s;
    }
}
