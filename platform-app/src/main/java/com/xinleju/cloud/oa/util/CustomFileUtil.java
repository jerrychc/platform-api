package com.xinleju.cloud.oa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

public class CustomFileUtil {
	/**
	 * 
	 * @param inputFileName
	 *            输入一个文件夹
	 * @param zipFileName
	 *            输出一个压缩文件夹，打包后文件名字
	 * @throws Exception
	 */
/*	public static void zip(String inputFileName, String zipFileName) throws Exception {
		zip(zipFileName, new File(inputFileName));
	}

	private static void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		zip(out, inputFile, "");
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) { // 判断是否为目录
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else { // 压缩目录中的所有文件
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
*/
	
	
	// 创建文件
		public static void createFile(String path, String fileName) {
			// path表示你所创建文件的路径
			File f = new File(path);
			/*
			 * if(!f.exists()){ f.mkdirs(); }
			 */
			// fileName表示你创建的文件名；为txt类型；
			File file = new File(f, fileName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}

		}
		    
	    /**
	    * 压缩文件列表中的文件
	    * @param files
	    * @param outputStream
	    * @throws IOException
	    */
		public static void zipFile(List<String> files, ZipOutputStream outputStream)
				throws IOException, ServletException {
			try {
				int size = files.size();
				// 压缩列表中的文件
				for (int i = 0; i < size; i++) {
					String pathName =  files.get(i);
					zipFile(pathName, outputStream);
				}
			} catch (IOException e) {
				throw e;
			}
		}
	   /**
		* 将文件写入到zip文件中
		* 
		* @param inputFile
		* @param outputstream
		* @throws Exception
		*/
		public static void zipFile(String pathName, ZipOutputStream outputstream)
				throws IOException, ServletException {
			try {
				if (!pathName.isEmpty()) {
					int i=pathName.lastIndexOf("/");
					String fileName = pathName.substring(i+1, pathName.length());
					   URL url = new URL(pathName);  
			           HttpURLConnection uc = (HttpURLConnection) url.openConnection();  
			           uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true  
			           uc.connect(); 
			           
			           InputStream iputstream = uc.getInputStream();
						BufferedInputStream bInStream = new BufferedInputStream(iputstream);
						ZipEntry entry = new ZipEntry(fileName);
						outputstream.putNextEntry(entry);

						final int MAX_BYTE = 10 * 1024 * 1024; // 最大的流为10M
						long streamTotal = 0; // 接受流的容量
						int streamNum = 0; // 流需要分开的数量
						int leaveByte = 0; // 文件剩下的字符数
						byte[] inOutbyte; // byte数组接受文件的数据

						streamTotal = bInStream.available(); // 通过available方法取得流的最大字符数
						streamNum = (int) Math.floor(streamTotal / MAX_BYTE); // 取得流文件需要分开的数量
						leaveByte = (int) streamTotal % MAX_BYTE; // 分开文件之后,剩余的数量

						if (streamNum > 0) {
							for (int j = 0; j < streamNum; ++j) {
								inOutbyte = new byte[MAX_BYTE];
								// 读入流,保存在byte数组
								bInStream.read(inOutbyte, 0, MAX_BYTE);
								outputstream.write(inOutbyte, 0, MAX_BYTE); // 写出流
							}
						}
						// 写出剩下的流数据
						inOutbyte = new byte[leaveByte];
						bInStream.read(inOutbyte, 0, leaveByte);
						outputstream.write(inOutbyte);
						outputstream.closeEntry(); // Closes the current ZIP entry
						// and positions the stream for
						// writing the next entry
						
						bInStream.close(); // 关闭
						iputstream.close();
				} else {
					throw new ServletException("文件不存在！");
				}
			} catch (IOException e) {
				throw e;
			}
		}
	      /**
			* 下载文件
			* @param file
			* @param response
			*/
	      public static HttpServletResponse downloadFile(File file,HttpServletResponse response,boolean isDelete) {
	          try {
	              // 以流的形式下载文件。
	              BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
	              byte[] buffer = new byte[fis.available()];
	              fis.read(buffer);
	              fis.close();
	              
	              // 清空response
	              response.reset();
	             
	              OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	              response.setContentType("application/octet-stream");
	              
	              response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
	              toClient.write(buffer);
	              toClient.flush();
	              toClient.close();
	              if(isDelete)
	              {
	             file.delete();        //是否将生成的服务器端文件删除
	              }
	           } 
	           catch (IOException ex) {
	              ex.printStackTrace();
	          }
	          return response;
	      }

}
