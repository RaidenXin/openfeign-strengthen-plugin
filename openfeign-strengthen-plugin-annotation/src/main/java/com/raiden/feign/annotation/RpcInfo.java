package com.raiden.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 0:26 2021/11/5
 * @Modified By:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcInfo {

    /**
     * 连接超时时间
     * @return
     */
    long connectTimeout();

    /**
     * 连接超时时间单位 默认是毫秒
     * @return
     */
    TimeUnit connectTimeoutUnit() default TimeUnit.MINUTES;

    /**
     * 读取超时
     */
    long readTimeout() ;

    /**
     * 读取超时时间单位 默认毫秒
     */
    TimeUnit readTimeoutUnit() default TimeUnit.MINUTES;;

    /**
     * 默认为不重定向
     * @return
     */
    boolean followRedirects() default false;

    /**
     * 切换实例的重试次数
     * @return
     */
    int maxAutoRetriesNextServer() default 0;

    /**
     * 对当前实例的重试次数
     * @return
     */
    int maxAutoRetries() default 0;

    /**
     * 是否允许重试
     * @return
     */
    boolean isAllowedRetry() default false;
}
