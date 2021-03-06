package com.xinleju.platform.uitls.openOffice;

/**
 * Created by lenovo on 2017/11/29.
 */

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @author Administrator
 *
 */
//@Component(value="threadManager")//交给Spring管理
public class ThreadManager {
    private ExecutorService executorService;

    public ThreadManager() {
        executorService = Executors.newFixedThreadPool(10);//初始化10的线程池，当执行的线程超过10，会等待线程池有空位
    }

    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }
}
