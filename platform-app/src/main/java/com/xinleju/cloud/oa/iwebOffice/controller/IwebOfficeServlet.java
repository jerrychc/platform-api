package com.xinleju.cloud.oa.iwebOffice.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentTempDtoServiceCustomer;

import sun.awt.AppContext;


/**
 *
 * @author 陈益特
 * @time  2015-01-09
 */

public class IwebOfficeServlet  extends HttpServlet{
    private IwebOfficeUtil MsgObj = new IwebOfficeUtil();
	String mOption;
	String mUserName;
	String mRecordID;
	String mFileName;
	String mFileType;
	byte[] mFileBody;
	int mFileSize = 0;
    String mFilePath; //取得服务器路径
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  mFilePath =request.getServletContext().getRealPath("/");  // request.getSession().getServletContext().getRealPath("");       //取得服务器路径
		   try{
			   if(request.getMethod().equalsIgnoreCase("POST")){//判断请求方式
				   MsgObj.setSendType("JSON");
				   MsgObj.Load(request); //解析请求
				   mOption = "SAVEFILE";//MsgObj.GetMsgByName("OPTION");//请求参数
				   mUserName = MsgObj.GetMsgByName("USERNAME");  //取得系统用户
				   System.out.println(mOption);
				   if(mOption.equalsIgnoreCase("LOADFILE")){
					    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
				        mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
				        MsgObj.MsgTextClear();//清除文本信息
				        System.out.println(mFilePath+"\\Document\\"+mFileName);
				        if (MsgObj.MsgFileLoad(mFilePath+"\\Document\\"+mFileName)){
				       // if (MsgObj.MsgFileLoad(mFilePath+"\\Document\\11.doc")){
				        	System.out.println(mRecordID+"文档已经加载");
				        }
				   }else if(mOption.equalsIgnoreCase("SAVEFILE")){

					   ApplicationContext context =new  ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
					   AttachmentTempDtoServiceCustomer attachmentTempService=(AttachmentTempDtoServiceCustomer)context.getBean("attachmentTempDtoServiceCustomer");

					   List<AttachmentTempDto> tempFile = new ArrayList<>();
					   //获取当前文件流大小
					   byte[] fileStrame = MsgObj.webOfficeFileSave("");
					   //实例附件对象
					   AttachmentTempDto att = new AttachmentTempDto();
					   //设置对象
					   att.setFileBytes(fileStrame);
					   tempFile.add(att);

					   String tempFileList = JacksonUtils.toJson(tempFile);
					   attachmentTempService.saveFileUpload(null, tempFileList);

					   System.out.println(mRecordID+"文档上传中");
					    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
				        mFileName = "officeFileUploadLoad";//MsgObj.GetMsgByName("FILENAME");//取得文档名称
				        MsgObj.MsgTextClear();//清除文本信息
				        if (MsgObj.MsgFileSave(mFilePath+"\\officeFileUpload\\")){
				        	//此处调用齐鹏的接口写入文件  AttachmentTempService attachmentTempService；
				        	System.out.println(mRecordID+"文档已经保存成功");
				        }
				   }else if(mOption.equalsIgnoreCase("SAVEPDF")){
					   System.out.println("文档转PDF");
					   mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
				       mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
				       MsgObj.MsgTextClear();//清除文本信息
				        if (MsgObj.MsgFileSave(mFilePath+"\\PDF\\"+mFileName)){
				        	System.out.println(mRecordID+"文档已经转换成功");
				        }

				   }
				 System.out.println("SendPackage");
				 MsgObj.Send(response);
			   }
			}catch (Exception e) {
				//e.printStackTrace();
		    }
	}

}
