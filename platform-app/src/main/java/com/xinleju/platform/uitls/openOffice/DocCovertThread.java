package com.xinleju.platform.uitls.openOffice;

import com.xinleju.platform.uitls.DocConverter;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ly on 2017/9/19.
 */
public class DocCovertThread implements Runnable{
    private static Logger logger = Logger.getLogger(DocCovertThread.class);

    @Override
    public void run() {
        try {
            String context = (String)TaskQueue.getTaskQueue().take();

            System.out.println("出队"+context);

            String tempPath = (String)context;
            if (tempPath == null) {
                return;
            }
            try{
                //转html
//                DocConverter docConverter = new DocConverter(tempPath);
//                docConverter.conver();
            }catch (Exception e){
                e.printStackTrace();
                logger.error("***** 异常信息 ***** 方法：switchDocToSwf at switch office to pdf", e); return;
            }finally{
                Consumer.isRunning=true;
            }
            System.out.println("转换结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Consumer.isRunning=true;
            logger.error("***** 异常信息 ***** 方法：DocConvertJob execute", e);
        }
    }

}
