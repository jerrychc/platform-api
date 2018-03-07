package com.xinleju.platform.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Version {
	
	/**
	 * 实体类对应的表名
	 * @return
	 */
	String value() default "";
	 /**
	  * 实体类的描述信息
	 * @return
	 */
	String desc() ;
}
