package com.xinleju.cloud.oa.iwebOffice.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.uitls.LoginUtils;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.operations.Bool;
import org.csource.common.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentTempDtoServiceCustomer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 金格Office组件
 *
 * @author wangw
 */
@Controller
@RequestMapping("/iwebOffice")
public class IwebOfficeController {

    private static Logger log = Logger.getLogger(IwebOfficeController.class);
    //知识目录APP暂时定位 CONTENT
    String appName = "CONTENT";
    //知识目录分类ID，暂时定为 -1；
    String appCategoryDOCId = "-1";
    String appCategoryHTMLId = "0";
    String suffix = "_doc";
    @Value("#{configuration['officeFilePath.linux']}")
    private String linuxOfficePath;
    @Value("#{configuration['officeFilePath.win']}")
    private String winOfficePath;
    @Value("#{configuration['pdfToHtmlProgramPath.win']}")
    private String winProgramPath;
    /**
     * 注入正文附件上传
     */
    @Autowired
    private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;

    @Autowired
    private AttachmentTempDtoServiceCustomer attachmentTempDtoServiceCustomer;
    /**
     * 定义附件初始化对象
     */
    private IwebOfficeUtil msgObj1 = new IwebOfficeUtil();

    /**
     * 文件上传处理
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult upload(MultipartHttpServletRequest request) {
        MessageResult result = new MessageResult();
        List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
        Iterator<String> itr = request.getFileNames();
        //记录接收正文转静态页面的值
        String fileName = null;
        try {
            while (itr.hasNext()) {

                final MultipartFile uploadFile = request.getFile(itr.next());

                final AttachmentTempDto fileMeta = new AttachmentTempDto();
                // 文件名
                String uploadFileName = uploadFile.getOriginalFilename();
                int dotIndex = uploadFileName.lastIndexOf('.');

                fileMeta.setName(uploadFileName.substring(0, dotIndex));
                fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
                fileMeta.setFullName(uploadFileName);
                fileMeta.setFileSize(uploadFile.getSize());
                // 元数据信息
                NameValuePair[] metaList = new NameValuePair[3];
                metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
                metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
                metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

                String[] upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
                        metaList);
//				String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};
                fileMeta.setPath(upResults[0] + "/" + upResults[1]);
                fileMeta.setUrl(upResults[2]);
                fileMeta.setId(IDGenerator.getUUID());
                fileMeta.setAppId(appName);
                fileMeta.setBusinessId(fileMeta.getName());
                fileMeta.setCategoryId(appCategoryDOCId);
                //fileMeta.setType(request.getParameter("type"));
                files.add(fileMeta);
                fileName = fileMeta.getName();
            }

            //保存正文时同时保存静态页面Start
            this.createHtml(request, fileName + ".html");
            //保存正文时同时保存静态页面End

            // 保存文件信息到附件临时表
            String saveJsonList = JacksonUtils.toJson(files);
            String dubboResultInfo = attachmentDtoServiceCustomer.saveBatch(null, saveJsonList);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
                    DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
                result.setResult(files);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用upload方法: 【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.SAVEERROR.isResult());
            result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }


    /**
     * 文件临时上传处理预览
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/tempUploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult tempUploadFile(MultipartHttpServletRequest request) {
        MessageResult result = new MessageResult();
        List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
        Iterator<String> itr = request.getFileNames();
        try {
            while (itr.hasNext()) {

                final MultipartFile uploadFile = request.getFile(itr.next());
                final AttachmentTempDto fileMeta = new AttachmentTempDto();
                // 文件名
                String uploadFileName = uploadFile.getOriginalFilename();
                int dotIndex = uploadFileName.lastIndexOf('.');

                fileMeta.setName(uploadFileName.substring(0, dotIndex));
                fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
                fileMeta.setFullName(uploadFileName);
                fileMeta.setFileSize(uploadFile.getSize());
                // 元数据信息
                NameValuePair[] metaList = new NameValuePair[3];
                metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
                metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
                metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

                String[] upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
                        metaList);
//				String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};
                fileMeta.setPath(upResults[0] + "/" + upResults[1]);
                fileMeta.setUrl(upResults[2]);
                fileMeta.setId(IDGenerator.getUUID());
                fileMeta.setAppId(appName);
                fileMeta.setBusinessId(fileMeta.getFullName());
                fileMeta.setCategoryId(appCategoryDOCId);
                //fileMeta.setType(request.getParameter("type"));
                files.add(fileMeta);
            }


            // 保存文件信息到附件临时表
            String saveJsonList = JacksonUtils.toJson(files);
            String dubboResultInfo = attachmentTempDtoServiceCustomer.saveBatch(null, saveJsonList);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
                    DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<AttachmentTempDto> attachmentTempDto = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentTempDto.class);
                result.setResult(files);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用upload方法: 【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.SAVEERROR.isResult());
            result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }


    /**
     * 上传静态页面到服务器目录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFileHtml", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult uploadFileHtml(MultipartHttpServletRequest request) {
        MessageResult result = new MessageResult();
        String mFilePath = request.getSession().getServletContext().getRealPath("/");
        String mRecordID = msgObj.GetMsgByName("RECORDID");                        //取得文档编号
        String mFileName = msgObj.GetMsgByName("FILENAME");//取得文档名称
        msgObj.MsgTextClear();//清除文本信息
        if (msgObj.MsgFileSave(mFilePath + "\\HTML\\" + mFileName)) {
            System.out.println(mRecordID + "文档已经转换成功");
        }
        return result;
    }

    /*
    private String uploadFDFS(HttpServletRequest request,HttpServletResponse response,byte[] mFileBody){
        List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
        Iterator<String> itr = request.getFileNames();
        Iterator<String> itr = request.getFileNames();
        //记录接收正文转静态页面的值
        String fileName = null;
            while (itr.hasNext()) {

                final MultipartFile uploadFile = request.getFile(itr.next());

                final AttachmentTempDto fileMeta = new AttachmentTempDto();
                // 文件名
                String uploadFileName = uploadFile.getOriginalFilename();
                int dotIndex = uploadFileName.lastIndexOf('.');

                fileMeta.setName(uploadFileName.substring(0, dotIndex));
                fileMeta.setExtendName(uploadFileName.substring(dotIndex + 1));
                fileMeta.setFullName(uploadFileName);
                fileMeta.setFileSize(uploadFile.getSize());
                // 元数据信息
                NameValuePair[] metaList = new NameValuePair[3];
                metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
                metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
                metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

                String[] upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
                        metaList);
                //String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};
                fileMeta.setPath(upResults[0] + "/" + upResults[1]);
                fileMeta.setUrl(upResults[2]);
                fileMeta.setId(IDGenerator.getUUID());
                fileMeta.setAppId(appName);
                fileMeta.setBusinessId(fileMeta.getName());
                fileMeta.setCategoryId(appCategoryId);
                //fileMeta.setType(request.getParameter("type"));
                files.add(fileMeta);
                fileName = fileMeta.getName();
            }

            //保存正文时同时保存静态页面Start
            this.createHtml(request, fileName + ".html");
            //保存正文时同时保存静态页面End

            // 保存文件信息到附件临时表
            String saveJsonList = JacksonUtils.toJson(files);
            String dubboResultInfo = attachmentDtoServiceCustomer.saveBatch(null, saveJsonList);
            return  null;
    }
    */

    private void createHtml(MultipartHttpServletRequest request, String mFileName) {
        String mFilePath = request.getSession().getServletContext().getRealPath("/");
        msgObj.MsgTextClear();//清除文本信息
        if (msgObj.MsgFileSave(mFilePath + "\\officeFileUpload\\" + mFileName)) {
            System.out.println("文档已经转换成功");
        }
    }

    /**
     * 编写上传office正文的方法
     *
     * @return
     */
    private String saveOfficeFile() {
        List<AttachmentTempDto> tempFile = new ArrayList<>();
        //获取当前文件流大小
        byte[] fileStrame = msgObj1.webOfficeFileSave("");
        //实例附件对象
        AttachmentTempDto att = new AttachmentTempDto();
        //设置对象
        att.setFileBytes(fileStrame);
        //将正文对象写入接口集合
        tempFile.add(att);
        //Json序列化对象
        String tempFileList = JacksonUtils.toJson(tempFile);
        //调用附件上传接口，无返回值
        // attachmentDtoServiceCustomer.saveFileUpload(null, tempFileList);
        //先测试功能通过，先返回NULL，然后再修改参数
        return null;
    }


    private IMsgServer2015 msgObj = new IMsgServer2015();

    @RequestMapping(value = "docService", method = RequestMethod.POST)
    public void service(MultipartHttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mOption = null;
        String mUserName = null;
        String mRecordID = null;
        String mFileName = null;
        String mFileType = null;
        byte[] mFileBody = null;
        String mGroup = null;
        String mGroupPath = null;
        String mFileGroupPath = null;
        int mFileSize = 0;
        String mFilePath; //取得服务器路径
        mFilePath = request.getSession().getServletContext().getRealPath("");       //取得服务器路径
        String officePath = OS.isLinux?linuxOfficePath:winOfficePath;
        String formDataJson = request.getParameter("FormData");

        if (formDataJson != null) {
            formDataJson = formDataJson.replaceAll("\'", "\"");
            //formDataJson = formDataJson.substring(1,formDataJson.length()-1);
            Map<String, Object> paramMap = JacksonUtils.fromJson(formDataJson, HashMap.class);
            mUserName = (String) paramMap.get("USERNAME");
            mRecordID = (String) paramMap.get("RECORDID");
            mFileName = (String) paramMap.get("FILENAME");
            mOption = (String) paramMap.get("OPTION");
            mFileType = (String) paramMap.get("FILETYPE");
            mGroup = (String) paramMap.get("GROUP");
            mFileGroupPath = (String) paramMap.get("FILEGROUPPATH");
        }
        //清除文件
        deleteAll(new File(officePath));
        try {
            if (request.getMethod().equalsIgnoreCase("POST")) {//判断请求方式
                msgObj.setSendType("JSON");
                msgObj.Load(request); //解析请求
                mOption = mOption == null ? msgObj.GetMsgByName("OPTION") : mOption;//请求参数
                mUserName = mUserName == null ? msgObj.GetMsgByName("USERNAME") : mUserName;  //取得系统用户
                if (mOption.equalsIgnoreCase("LOADFILE")) {
                    msgObj.MsgTextClear();//清除文本信息
                    System.out.println(mFilePath + "\\Document\\" + mFileName);
                    //String tempPath = System.getProperty("java.io.tmpdir")+File.separator+mFileName;//临时文件目录
                    String tempPath = officePath + File.separator + mFileName;//临时文件目录
                    String group = (mGroup.indexOf("/") > -1) ? mGroup.substring(0, mGroup.indexOf("/")) : "";
                    String fileName = (mGroup.lastIndexOf("/") > -1) ? mGroup.substring(mGroup.indexOf("/") + 1) : "";
                    try {
                        int result = new FastDFSClient().download(group, fileName, tempPath);//下载附件服务器上的文件到临时目录
                    } catch (Exception e1) {

                    }

                    //msgObj.MsgFileLoad("C:\\Users\\luoro\\Desktop\\ChEDyFjKVeuAVrngAABWAPKU9uM799.doc")
                    if (msgObj.MsgFileLoad(tempPath)) {//加载临时目录下的文件  msgObj.MsgFileLoad(tempPath)
                        // if (MsgObj.MsgFileLoad(mFilePath+"\\Document\\11.doc")){
                        System.out.println(mRecordID + "文档已经加载");
                    }

                } else if (mOption.equalsIgnoreCase("SAVEFILE")) {
                	long savefile_starttime = System.currentTimeMillis();
                    System.out.println(mRecordID + "文档上传中");
                 /*   mRecordID = msgObj.GetMsgByName("RECORDID");                        //取得文档编号
                    mFileName = msgObj.GetMsgByName("FILENAME");//取得文档名称*/
                    msgObj.MsgTextClear();//清除文本信息
                    /*if (msgObj.MsgFileSave(mFilePath+"\\Document\\"+mFileName)){
                        System.out.println(mRecordID+"文档已经保存成功");
                    }*/

                    clearOldData(mRecordID);//清空文档缓存数据

                    /**/
                    List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
                    Iterator<String> itr = request.getFileNames();
                    //记录接收正文转静态页面的值
                    String fileName = null;
                    while (itr.hasNext()) {

                        final MultipartFile uploadFile = request.getFile(itr.next());

                        final AttachmentTempDto fileMeta = new AttachmentTempDto();
                        // 文件名
                        String uploadFileName = uploadFile.getOriginalFilename();
                        int dotIndex = uploadFileName.lastIndexOf('.');

                        String extendName = uploadFileName.substring(dotIndex + 1);//后缀名
                        
                        fileMeta.setName(uploadFileName.substring(0, dotIndex));
                        fileMeta.setExtendName(extendName);
                        fileMeta.setFullName(uploadFileName);
                        fileMeta.setFileSize(uploadFile.getSize());


                        // 元数据信息
                        NameValuePair[] metaList = new NameValuePair[3];
                        metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
                        metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
                        metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));

                        try {
                            String[] upResults = new String[3];
                            upResults = new FastDFSClient().upload(uploadFile.getBytes(), fileMeta.getExtendName(),
                                    metaList);
                            fileMeta.setPath(upResults[0] + "/" + upResults[1]);
                            fileMeta.setUrl(upResults[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
        				//String[] upResults = {"group1","M00/00/00/wKiZC1irIkeAWka8AAc9Nx4lZAs201.zip","192.168.1.2"};

                        fileMeta.setId(IDGenerator.getUUID());
                        fileMeta.setAppId(appName);
                        fileMeta.setBusinessId(fileMeta.getName()+suffix);
                        fileMeta.setCategoryId("doc".equals(extendName)?appCategoryDOCId:appCategoryHTMLId);
                        //fileMeta.setType(request.getParameter("type"));
                        files.add(fileMeta);
                        fileName = fileMeta.getName();

                        //保存正文时同时保存静态页面Start
                       // this.createHtml(request, fileName + ".html");

                        String dubboResultInfo = attachmentDtoServiceCustomer.save(getUserJson(), JacksonUtils.toJson(fileMeta));

                    }
                    long savefile_endtime = System.currentTimeMillis();

                    System.out.println("保存SAVEFILE耗时===========》  "+(savefile_endtime-savefile_starttime)+" 秒");

                } else if (mOption.equalsIgnoreCase("SAVEPDF")) {
                	msgObj.Send(response);//运行时间过长，先返回前端信息
                	long savepdf_starttime = System.currentTimeMillis();
                	System.out.println(mRecordID + "PDF文档上传中");
                    List<AttachmentTempDto> files = new LinkedList<AttachmentTempDto>();
                    Iterator<String> itr = request.getFileNames();
                    //记录接收正文转静态页面的值
                    String fileName = null;
                    while (itr.hasNext()) {

                        final MultipartFile uploadFile = request.getFile(itr.next());
                        
                        String tempPath = officePath + File.separator + mFileName;//临时文件目录
                        String htmlName = tempPath.substring(tempPath.lastIndexOf(File.separator)+1,tempPath.lastIndexOf('.')+1)+"html";
                        //这是你要保存之后的文件，是自定义的，本身不存在
                        File afterfile = new File(tempPath.replace("//", "\\"));
                        
                        //定义文件输出流，用来把信息写入afterfile文件中
                        FileOutputStream fos = new FileOutputStream(afterfile);
                        try { 
	                    	fos.write(uploadFile.getBytes());
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }finally{
                        	fos.flush();
                        	fos.close();
                        }

                        //根据系统调用pdf转html方法
                        if(OS.isLinux){
                        	Pdf2htmlEXUtil.pdf2html_linux(tempPath.replace("//", "\\"), officePath.replace("//", "\\"), htmlName);
                        }else{
                        	Pdf2htmlEXUtil.pdf2html(winProgramPath,tempPath.replace("//", "\\"),  officePath.replace("//", "\\"), htmlName);
                        }
                        byte[] bytes = getBytes(officePath + File.separator + htmlName);
                        
                        final AttachmentTempDto fileMeta = new AttachmentTempDto();
                        // 文件名
                        String uploadFileName = uploadFile.getOriginalFilename();
                        int dotIndex = uploadFileName.lastIndexOf('.');

                        fileMeta.setName(uploadFileName.substring(0, dotIndex));
                        fileMeta.setExtendName("html");
                        fileMeta.setFullName(uploadFileName.substring(0, dotIndex)+".html");
                        fileMeta.setFileSize(Long.valueOf(String.valueOf(bytes.length)));
                        // 元数据信息
                        NameValuePair[] metaList = new NameValuePair[3];
                        metaList[0] = new NameValuePair("fileName", fileMeta.getFullName());
                        metaList[1] = new NameValuePair("fileExtName", fileMeta.getExtendName());
                        metaList[2] = new NameValuePair("fileLength", String.valueOf(fileMeta.getFileSize()));


                        try {
                            String[] upResults = new String[3];
                            upResults = new FastDFSClient().upload(bytes, fileMeta.getExtendName(),
                                    metaList);
                            fileMeta.setPath(upResults[0] + "/" + upResults[1]);
                            fileMeta.setUrl(upResults[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fileMeta.setId(IDGenerator.getUUID());
                        fileMeta.setAppId(appName);
                        fileMeta.setBusinessId(fileMeta.getName());
                        fileMeta.setCategoryId(appCategoryHTMLId);
                        files.add(fileMeta);
                        fileName = fileMeta.getName();

                        String dubboResultInfo = attachmentDtoServiceCustomer.save(getUserJson(), JacksonUtils.toJson(fileMeta));

                    }
                     long savepdf_endtime = System.currentTimeMillis();
                     System.out.println("保存SAVEPDF耗时========= "+(savepdf_endtime-savepdf_starttime)+" 豪秒");

                }
                System.out.println("SendPackage");
                msgObj.Send(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除文件缓存数据
     */
    private void clearOldData(String mRecordID){
        /**清空附件服务器历史文档数据开始*/
        try {
            Map<String, Object> param = new HashedMap ();
            param.put ("businessId", mRecordID+suffix);
            param.put ("appId", appName);
            param.put ("categoryId", appCategoryDOCId);
            String resultJson = attachmentDtoServiceCustomer.queryList (getUserJson (), JacksonUtils.toJson (param));
            //清空doc文件
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson, DubboServiceResultInfo.class);
            List<AttachmentDto> list = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (), List.class, AttachmentDto.class);
            if (list != null && !list.isEmpty ()) {
                StringBuffer ids = new StringBuffer ();
                for (AttachmentDto attachmentDto : list) {
                    ids.append (attachmentDto.getId ()).append (",");
                    if (StringUtils.isNotEmpty (attachmentDto.getPath ())) {
                        String groupName = attachmentDto.getPath ().substring (0, attachmentDto.getPath ().indexOf ('/'));
                        String remoteFileName = attachmentDto.getPath ().substring (attachmentDto.getPath ().indexOf ('/') + 1);
                        new FastDFSClient ().delete (groupName, remoteFileName);
                    }

                }
                Map<String, String> pa = new HashMap ();
                pa.put ("id", ids.toString ());
                attachmentDtoServiceCustomer.deleteAllObjectByIds (getUserJson (), JacksonUtils.toJson (pa));
            }

          //清空mht文件
            param.put ("categoryId", appCategoryHTMLId);
            String resultJson2 = attachmentDtoServiceCustomer.queryList (getUserJson (), JacksonUtils.toJson (param));
            DubboServiceResultInfo dubboServiceResultInfo2 = JacksonUtils.fromJson (resultJson2, DubboServiceResultInfo.class);
            List<AttachmentDto> list2 = JacksonUtils.fromJson (dubboServiceResultInfo2.getResult (), List.class, AttachmentDto.class);
            if (list2 != null && !list2.isEmpty ()) {
                StringBuffer ids = new StringBuffer ();
                for (AttachmentDto attachmentDto : list2) {
                    ids.append (attachmentDto.getId ()).append (",");
                    if (StringUtils.isNotEmpty (attachmentDto.getPath ())) {
                        String groupName = attachmentDto.getPath ().substring (0, attachmentDto.getPath ().indexOf ('/'));
                        String remoteFileName = attachmentDto.getPath ().substring (attachmentDto.getPath ().indexOf ('/') + 1);
                        new FastDFSClient ().delete (groupName, remoteFileName);
                    }

                }
                Map<String, String> pa = new HashMap ();
                pa.put ("id", ids.toString ());
                attachmentDtoServiceCustomer.deleteAllObjectByIds (getUserJson (), JacksonUtils.toJson (pa));
            }
        }catch(Exception e){

        }
        /**清空附件服务器历史文档数据结束*/
    }
    @RequestMapping(value = "docPreview", method = RequestMethod.POST)
    public void docPreview(MultipartHttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mOption = null;
        String mUserName = null;
        String mRecordID = null;
        Boolean isMinVersion = false;
        String mFilePath = request.getSession().getServletContext().getRealPath("")+File.separator+"officeFiles"+File.separator+"docPreview";       //取得服务器路径
        String formDataJson = request.getParameter("FormData");
        if (formDataJson != null) {
            formDataJson = formDataJson.replaceAll("\'", "\"");
            Map<String, Object> paramMap = JacksonUtils.fromJson(formDataJson, HashMap.class);
            mUserName = (String) paramMap.get("USERNAME");
            mRecordID = (String) paramMap.get("RECORDID");
            mOption = (String) paramMap.get("OPTION");
            isMinVersion = Boolean.valueOf((String)paramMap.get("ISMINVERSION"));
        }
        //清除文件
        deleteAll(new File(mFilePath));//html 文件
        try {
            if (request.getMethod().equalsIgnoreCase("POST")) {//判断请求方式
                msgObj.setSendType("JSON");
                msgObj.Load(request); //解析请求
                mOption = mOption == null ? msgObj.GetMsgByName("OPTION") : mOption;//请求参数
                mUserName = mUserName == null ? msgObj.GetMsgByName("USERNAME") : mUserName;  //取得系统用户
                if (mOption.equalsIgnoreCase("PREVIEW")) {
                    Iterator<String> itr = request.getFileNames();
                    //记录接收正文转静态页面的值
                    while (itr.hasNext()) {

                        final MultipartFile uploadFile = request.getFile(itr.next());
                        // 文件名
                        String uploadFileName = uploadFile.getOriginalFilename();
                        /**
                         * 保存为mht文件
                        String tempPath = mFilePath +File.separator + uploadFileName;//临时文件目录
                        //这是你要保存之后的文件，是自定义的，本身不存在
                        File afterfile = new File(tempPath);
                        //定义文件输出流，用来把信息写入afterfile文件中
                        FileOutputStream fos = new FileOutputStream(afterfile);
                        try {
                        	fos.write(uploadFile.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally{
                        	fos.flush();
                        	fos.close();
                        }
                        */
                        //转html
                        HtmlApplication.mht2html(uploadFile.getInputStream(), mFilePath+File.separator + uploadFileName.substring(0,uploadFileName.lastIndexOf("."))+".html",isMinVersion);
                    }
                }
                System.out.println("SendPackage");
                msgObj.Send(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();

        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }

    @RequestMapping(value = "getHtmlPath", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult getHtmlPath(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) {
    	long getHtml_starttime = System.currentTimeMillis();
        MessageResult result = new MessageResult();
        String mGroup = null;
        String mFileName = null;
        String NAME = null;
        Boolean isMinVersion = false;
        String mFilePath = request.getSession().getServletContext().getRealPath("") + File.separator + "officeFiles";       //取得服务器路径

        String paramaterJson = JacksonUtils.toJson(map);
        if (paramaterJson != null) {
            Map<String, Object> paramMap = JacksonUtils.fromJson(paramaterJson, HashMap.class);
            mFileName = (String) paramMap.get("FILENAME");
            mGroup = (String) paramMap.get("GROUP");
            NAME = (String) paramMap.get("NAME");
            isMinVersion = (Boolean) paramMap.get("ISMINVERSION");
        }
        try {
            String tempPath = mFilePath + File.separator + NAME+".mht";//临时文件目录
            String group = (mGroup.indexOf("/") > -1) ? mGroup.substring(0, mGroup.indexOf("/")) : "";
            String fileName = (mGroup.lastIndexOf("/") > -1) ? mGroup.substring(mGroup.indexOf("/") + 1) : "";
            
            int ret = new FastDFSClient().download(group, fileName, tempPath);//下载附件服务器上的文件到临时目录
          //转html
            HtmlApplication.mht2html(tempPath, tempPath.substring(0,tempPath.lastIndexOf("."))+".html",isMinVersion);
        } catch (Exception e) {
            log.error("word转HTML失败",e);
        }
        long getHtml_endtime = System.currentTimeMillis();
        System.out.println("getHtmlPath耗时========= "+(getHtml_endtime-getHtml_starttime)/1000+" 秒");
        result.setSuccess(MessageInfo.GETSUCCESS.isResult());
        result.setMsg("officeFiles" + File.separator + NAME+".html");
        return result;
    }
    /**
     * 删除文件夹下文件
     * @param oldPath
     */
    public static void deleteAll(File oldPath){
    	if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
            	deleteAll(file);
            }
            oldPath.delete();
           }else{
             oldPath.delete();
        }
    }
}
