package com.xinleju.platform.uitls.openOffice;

/**
 * Created by ly on 2017/9/19.
 */
public class Produce implements Runnable {
    private static volatile boolean isRunning=true;
    private static volatile Object obj;
    public Produce(Object obj){
        this.obj = obj;
    }
    public void run() {
        TaskQueue.add(obj);
    }

}
