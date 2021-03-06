package com.xinleju.platform.uitls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//操作对象日志
@Target(ElementType.METHOD) //注解用于什么地方
@Retention(RetentionPolicy.RUNTIME) //什么时候使用该注解
public @interface OpeLogInfo {
   public String sysCode() default "PT";//系统code
   public String node() default ""  ;//操作对象
}
