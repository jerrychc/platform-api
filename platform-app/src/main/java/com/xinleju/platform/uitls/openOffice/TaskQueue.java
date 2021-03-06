package com.xinleju.platform.uitls.openOffice;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ly on 2017/9/19.
 */
public class TaskQueue {
    private static LinkedBlockingQueue queues = null;

    public static LinkedBlockingQueue getTaskQueue(){
        if(queues==null){
            queues =  new LinkedBlockingQueue();
            System.out.println("初始化 队列");
        }
        return queues;
    }

    public static void add(Object obj){
        if(queues==null)
            queues =  getTaskQueue();
        if(!queues.contains(obj))
            queues.offer(obj);
        System.out.println("-------------------------------");
        System.out.println("入队："+obj);
    }
}
