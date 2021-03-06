package com.xinleju.cloud.oa.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * 修改文件
 */
public class FileModify {

    /**
     * 读取文件内容
     * 
     * @param filePath
     * @return
     */
    public String read(String filePath) {
    	
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();
        
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                if (line.contains("charset=gb2312")) {
                	line = "<meta http-equiv=Content-Type content='text/html; charset=utf-8'>";
                	buf.append(line);
                }else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        
        return buf.toString();
    }
    
    /**
     * 将内容回写到文件中
     * 
     * @param filePath
     * @param content
     */
    public void write(String filePath, String content) {
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(content);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        String filePath = FileModify.class.getResource("").getPath()+"001.html"; // 文件路径
        //String filePath = " /content/officeFileUpload/001.html";
        FileModify obj = new FileModify();
        obj.write(filePath, obj.read(filePath)); // 读取修改文件
        System.out.println("修改完毕！");
    }

}