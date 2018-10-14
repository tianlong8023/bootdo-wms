package com.bootdo.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormat {

	String pattern() default "yyyy-MM-dd HH:mm:ss";

}
