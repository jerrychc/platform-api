package com.xinleju.platform.uitls.openOffice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * Created by lenovo on 2017/11/29.
 *
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)转化为html格式或者pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 */
public class Doc2HtmlUtil {
    private final Logger logger = Logger.getLogger(Doc2HtmlUtil.class);

    private static String OpenOffice_HOME = ConfigurationUtil.OPENOFFICE_HOME;//"D:\\Program Files\\OpenOffice.org 3";
    private static String portsStr = ConfigurationUtil.OPENOFFICE_PORT;

//    private final String OpenOffice_HOME = PropertiesUtils.getVal("tools.properties", "OpenOffice_HOME");

    private List<Process> process = new ArrayList<Process>();//process集合，方便服务器关闭时，关闭openoffice进程

    public BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();//这里用线程安全的queue管理运行的端口号

    public void startAllService() throws IOException, NumberFormatException, InterruptedException{

        String[] ports = portsStr.split(",");

        for (String port : ports) {
            //添加到队列 用于线程获取端口 进行连接
            queue.put(Integer.parseInt(port));
            //启动OpenOffice的服务
            String command = "cd /opt/openoffice4/program;soffice -headless -accept=\"socket,host=127.0.0.1,port="+port+";urp;\"";//这里根据port进行进程开启
            process.add(Runtime.getRuntime().exec(command));
            logger.debug("[startAllService-port-["+port+"]-success]");
        }
        logger.debug("[startAllService-success]");
    }

    public void stopAllService(){
        for (Process p : process) {
            p.destroy();
        }
        logger.debug("[stopAllService-success]");
    }

    /**
     * 根据端口获取连接服务  每个转换操作时，JodConverter需要用一个连接连接到端口，（这里类比数据库的连接）
     * */
    public OpenOfficeConnection getConnect(int port) throws ConnectException{
        logger.debug("[connectPort-port:"+port+"]");
        return new SocketOpenOfficeConnection(port);
    }

    /**
     * 根据端口获取连接服务  每个转换操作时，JodConverter需要用一个连接连接到端口，（这里类比数据库的连接）
     * */
    public OpenOfficeConnection getConnect(String hostPath,int port) throws ConnectException{
        logger.debug("[connectPort-port:"+port+"]");
        return new SocketOpenOfficeConnection(hostPath,port);
    }

    public Doc2HtmlUtil(){
    }

    /**
     * 转换文件成html
     *
     * @param fromFileInputStream:
     * @throws IOException
     * @throws InterruptedException
     */
    public String file2Html(InputStream fromFileInputStream, String toFilePath, String type) throws IOException, InterruptedException {
        String methodName = "[file2Html]";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = UUID.randomUUID().toString()+sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if ("doc".equals(type) || "docx".equals(type)) {
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".html";
        } else if ("xls".equals(type) || "xlsx".equals(type)) {
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".html";
        } else if ("ppt".equals(type) || "pptx".equals(type)) {
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".html";
        } else {
            return null;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();

        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }
        //这里是重点，每次转换从集合读取一个未使用的端口（直接拿走，这样其他线程就不会读取到这个端口号，不会尝试去使用）</span>
        //计时并读取一个未使用的端口
        long old = System.currentTimeMillis();
        int port = queue.take();
        //获取并开启连接
        OpenOfficeConnection connection = getConnect("127.0.0.1",port);
        connection.connect();
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        try {
            converter.convert(docInputFile, htmlOutputFile);
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
        //关闭连接
        connection.disconnect();
        //计算花费时间 将端口放入池中
        System.out.println(Thread.currentThread().getName() + "disConnect-port=" + port + "-time=" + (System.currentTimeMillis() - old));
        queue.put(port);
        //端口号使用完毕之后 放回队列中，其他线程有机会使用</span>

        // 转换完之后删除word文件
        docInputFile.delete();
        logger.debug(methodName + "htmFileName:" + htmFileName);
        return htmFileName;
    }

    /**
     * 转换文件成pdf
     *
     * @param fromFileInputStream:
     * @throws IOException
     * @throws InterruptedException
     */
    public String file2pdf(InputStream fromFileInputStream, String toFilePath, String type) throws IOException, InterruptedException {
        String methodName = "[file2pdf]";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = UUID.randomUUID().toString()+sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if ("doc".equals(type) || "docx".equals(type)) {
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".pdf";
        } else if ("xls".equals(type) || "xlsx".equals(type)) {
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".pdf";
        } else if ("ppt".equals(type) || "pptx".equals(type)) {
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".pdf";
        } else {
            return null;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }

        //计时并读取一个未使用的端口
        long old = System.currentTimeMillis();
        int port = queue.take();
        //获取并开启连接
        OpenOfficeConnection connection = getConnect(port);
        connection.connect();
        //OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        try {
            converter.convert(docInputFile, htmlOutputFile);
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
        //关闭连接
        connection.disconnect();
        //计算花费时间 将端口放入池中
        System.out.println(Thread.currentThread().getName() + "disConnect-port=" + port + "-time=" + (System.currentTimeMillis() - old));
        queue.put(port);
        // 转换完之后删除word文件
        docInputFile.delete();
        logger.debug(methodName + "htmFileName:" + htmFileName);
        return htmFileName;
    }

    /**
     * 转换文件成pdf
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void file2pdf(File docFile, File pdfFile) throws IOException, InterruptedException {
        startAllService();
        //计时并读取一个未使用的端口
        long old = System.currentTimeMillis();
        int port = queue.take();
        //获取并开启连接
        OpenOfficeConnection connection = getConnect(port);
        connection.connect();
        //OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        try {
            converter.convert(docFile, pdfFile);
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
        //关闭连接
        connection.disconnect();
        //计算花费时间 将端口放入池中
        System.out.println(Thread.currentThread().getName() + "disConnect-port=" + port + "-time=" + (System.currentTimeMillis() - old));
        queue.put(port);
        // 转换完之后删除word文件
        docFile.delete();
        logger.debug("转换html成功");
        stopAllService();
    }
}