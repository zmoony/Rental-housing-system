package com.zufang.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录日志的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    /**
     * 操作类型
     */
    String operationType() default "";
    
    /**
     * 操作描述
     */
    String operationDesc() default "";
}