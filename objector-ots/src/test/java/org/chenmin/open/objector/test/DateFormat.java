package org.chenmin.open.objector.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解类
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface DateFormat {
	String value() default "yyyy-MM-dd";
}
