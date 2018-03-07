package com.xinleju.platform.uitls.openOffice;

import java.io.IOException;

/**
 * Created by lenovo on 2017/11/30.
 */
public class Main {
    public static void main(String[] args){
        String command = "/opt/openoffice4/program/soffice -headless -accept=\"socket,host=127.0.0.1,port=81000;urp;\"";//这里根据port进行进程开启
        try {
            Process pro = Runtime.getRuntime().exec(command);
            System.out.println(pro.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
