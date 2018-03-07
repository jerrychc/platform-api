package com.xinleju.platform.uitls;
import java.io.*;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.xinleju.platform.sys.res.controller.DataCtrlController;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * doc docx格式转换
 */
public class DocConverter {

    private static Logger log = Logger.getLogger(DataCtrlController.class);
    private static final int environment = 1;// 环境 1：windows 2:linux
    private String fileString;// (只涉及pdf2swf路径问题)
    private String pdfFilePath = "";
    private String outputPath = "";
    private String filePath = "";
    private String fileName;
    private String extensionName;
    private File pdfFile;
    private File swfFile;
    private File docFile;

    private RedisTemplate<String, String> redisTemplate;

    public DocConverter(String fileString,RedisTemplate<String, String> redisTemplate) {
        ini(fileString,redisTemplate);
    }

    /**
     * 重新设置file
     *
     * @param fileString
     */
    public void setFile(String fileString,RedisTemplate<String, String> redisTemplate) {
        ini(fileString,redisTemplate);
    }

    /**
     * 初始化
     *
     * @param fileString
     */
    private void ini(String fileString,RedisTemplate<String, String> redisTemplate) {
        try {
            this.redisTemplate = redisTemplate;
            this.fileString = fileString;
            this.fileName = fileString.substring(0, fileString.lastIndexOf("."));
            this.filePath = fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.length());
            File parent = new File(fileName);
            log.info("openOffice****office转换html，创建html文件夹开始****");
            parent.mkdirs();
            if (!parent.exists()){   //创建文件夹失败的话则退出
                return;
            }
            log.info("openOffice****office转换html，创建html文件夹结束****");
            // 用于处理TXT文档转化为PDF格式乱码,获取上传文件的名称（不需要后面的格式）
            String txtName = fileString.substring(fileString.lastIndexOf("."));
            // 判断上传的文件是否是TXT文件
            if (txtName.equalsIgnoreCase(".txt")) {
                // 定义相应的ODT格式文件名称
                docFile = new File(fileName + ".odt");
                // 将上传的文档重新copy一份，并且修改为ODT格式，然后有ODT格式转化为PDF格式
                this.copyFile(new File(fileString), docFile);
            }else{
                docFile = new File(fileString);
            }
            log.info("openOffice****office转换html，创建html文件开始****");
            if("*.pdf".indexOf(fileString.substring(fileString.lastIndexOf(".")+1).toLowerCase())>-1){
                extensionName = null;
            }else if("*.ppt,*.pptx".indexOf(fileString.substring(fileString.lastIndexOf(".")+1).toLowerCase())>-1){
                extensionName = ".pdf";
            }else{
                extensionName = ".html";
            }
            if(extensionName!=null){
                pdfFilePath = fileName+File.separator+ filePath + extensionName;
                pdfFile = new File(pdfFilePath);
            }
            log.info("openOffice****office转换html，创建html文件结束****");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转为PDF
     *
     */
    private void doc2pdf() throws Exception {
        if (docFile.exists()) {
            if (!pdfFile.exists()) {
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                try {
                    connection.connect();
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                    converter.convert(docFile, pdfFile);
                    // close the connection
                    connection.disconnect();
                    log.info("openOffice****pdf转换成功，PDF输出：" + pdfFile.getPath()+ "****");
                } catch (java.net.ConnectException e) {
                    e.printStackTrace();
                    log.error("openOffice****swf转换器异常，openoffice服务未启动！****");
                    throw e;
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                    e.printStackTrace();
                    log.error("openOffice****swf转换器异常，读取转换文件失败****");
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                log.info("openOffice****已经转换为pdf，不需要再进行转化****");
            }
        } else {
            log.error("openOffice****swf转换器异常，需要转换的文档不存在，无法转换****");
        }
    }

    /**
     * 转为PHTML
     *
     */
    private Boolean doc2html() throws Exception {
        if (docFile.exists()) {
            if (!pdfFile.exists()) {
                    OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
                    try {
                        connection.connect();
                        log.error("连接openoffice服务成功！****");
                        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
                        log.error("读取文件path，docFile:" + docFile + "产生文件path：pdfFile:" + pdfFile);
                        converter.convert(docFile, pdfFile);
                        connection.disconnect();
                        log.info("openOffice****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");
                        return true;
                    } catch (java.net.ConnectException e) {
                        e.printStackTrace();
                        log.error("openOffice****swf转换器异常，openoffice服务未启动！****");
                        return false;
                    } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                        e.printStackTrace();
                        log.error("openOffice****swf转换器异常，读取转换文件失败****");
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    } finally {
                        try {
                            if (connection != null) {
                                connection.disconnect();
                                connection = null;
                            }
                        } catch (Exception e) {
                        }
                    }
            }else{
                log.info("openOffice****已经转换为pdf，不需要再进行转化****");
                return true;
            }
        } else {
            log.error("openOffice****swf转换器异常，需要转换的文档不存在，无法转换****");
            return false;
        }
    }

    /**
     * 转为PHTML
     *
     */
    private boolean doc2html1() throws Exception {
        // 验证二维码有效性
        Boolean hasQR = redisTemplate.hasKey(filePath);
        if(hasQR == true){
            return false;
        }
        System.out.println("准备启动服务....");
        try {
            System.out.println("尝试连接已启动的服务...");
            OfficeManager officeManager;
            try {
                System.out.println("尝试连接已启动的服务...");
                ExternalOfficeManagerConfiguration externalProcessOfficeManager = new ExternalOfficeManagerConfiguration();
                externalProcessOfficeManager.setConnectOnStart(true);
                externalProcessOfficeManager.setPortNumber(8100);
                officeManager = externalProcessOfficeManager.buildOfficeManager();
                officeManager.start();
                System.out.println("office转换服务启动成功!");
                OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
                converter.convert(docFile,pdfFile);
                officeManager.stop();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("没有已启动的服务...");
            }
            System.out.println("创建并连接新服务...");
            DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
            System.out.println("准备启动服务....");
            configuration.setOfficeHome("/opt/openoffice4");//设置OpenOffice.org安装目录
            configuration.setPortNumbers(8100); //设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 3 * 1L);//设置任务执行超时为30秒
            configuration.setTaskQueueTimeout(1000 * 10 * 1L);//设置任务队列超时为1分钟
            officeManager = configuration.buildOfficeManager();

            officeManager.start();    //启动服务
            System.out.println("office转换服务启动成功!");
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            converter.convert(docFile,pdfFile);
            officeManager.stop();
            return true;
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
            if(!filePath.isEmpty()){
                redisTemplate.opsForValue().set(filePath,JacksonUtils.toJson(InetAddress.getLocalHost()));
            }
            return false;
        }

    }

    /**
     * 转换成 swf
     */
    @SuppressWarnings("unused")
    private void pdf2swf() throws Exception {
        Runtime r = Runtime.getRuntime();
        if (!swfFile.exists()) {
            if (pdfFile.exists()) {
                if (environment == 1) {// windows环境处理
                    try {
                        Process p = r.exec("D:/Program Files (x86)/SWFTools/pdf2swf.exe "+ pdfFile.getPath() + " -o "+ swfFile.getPath() + " -T 9");
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.print(loadStream(p.getErrorStream()));
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.println("****swf转换成功，文件输出："
                                + swfFile.getPath() + "****");
                        if (pdfFile.exists()) {
                            pdfFile.delete();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }
                } else if (environment == 2) {// linux环境处理
                    try {
                        Process p = r.exec("pdf2swf " + pdfFile.getPath()
                                + " -o " + swfFile.getPath() + " -T 9");
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.print(loadStream(p.getErrorStream()));
                        System.err.println("****swf转换成功，文件输出："
                                + swfFile.getPath() + "****");
                        if (pdfFile.exists()) {
                            pdfFile.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            } else {
                System.out.println("****pdf不存在,无法转换****");
            }
        } else {
            System.out.println("****swf已经存在不需要转换****");
        }
    }

    static String loadStream(InputStream in) throws IOException {

        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();

        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }

        return buffer.toString();
    }
    /**
     * 转换主方法
     */
    @SuppressWarnings("unused")
    public String conver() {
        if (environment == 1) {
            log.info("openOffice****swf转换器开始工作，当前设置运行环境windows****");
        } else {
            log.info("openOffice****swf转换器开始工作，当前设置运行环境linux****");
        }
        try {
            if(extensionName!=null){
                boolean bl = doc2html1();
                if(bl){
                    return pdfFilePath;
                }else{
                    return "";
                }
            }else{
                return fileString;
            }
            //pdf2swf();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Title: copyFile
     * @Description: TODO
     * @param: @param docFile2
     * @param: @param odtFile2
     * @return: void
     * @throws
     */
    private void copyFile(File sourceFile,File targetFile)throws Exception{
        //新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        //新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff  = new BufferedOutputStream(output);
        //缓冲数组
        byte[]b = new byte[1024 * 5];
        int len;
        while((len = inBuff.read(b)) != -1){
            outBuff.write(b,0,len);
        }
        //刷新此缓冲的输出流
        outBuff.flush();
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 返回文件路径
     *
     */
    public String getswfPath() {
        if (swfFile.exists()) {
            String tempString = swfFile.getPath();
            tempString = tempString.replaceAll("\\\\", "/");
            return tempString;
        } else {
            return "";
        }
    }
    /**
     * 设置输出路径
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        if (!outputPath.equals("")) {
            String realName = fileName.substring(fileName.lastIndexOf("/"),
                    fileName.lastIndexOf("."));
            if (outputPath.charAt(outputPath.length()) == '/') {
                swfFile = new File(outputPath + realName + ".swf");
            } else {
                swfFile = new File(outputPath + realName + ".swf");
            }
        }
    }

    public static void main(String[] args) {
//        DocConverter docConverter = new DocConverter("C:\\Users\\lenovo\\Desktop\\111.xlsx");
//        docConverter.conver();
    }
}
