package com.raiden.feign;

import com.raiden.feign.annotation.RpcInfo;
import com.raiden.feign.properties.ReinforceFeignProperties;
import com.raiden.feign.properties.ReinforceOptions;
import com.raiden.feign.utils.FieldUtils;
import feign.InvocationHandlerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 17:18 2021/11/6
 * @Modified By:
 */
public abstract class AbstractReinforceInvocationHandler {

    private ReinforceFeignProperties properties;

    public AbstractReinforceInvocationHandler(ReinforceFeignProperties properties){
        this.properties = properties;
    }

    protected void initRpcInfo(Map<Method, InvocationHandlerFactory.MethodHandler> dispatch){
        final Map<String, ReinforceOptions> rpcConfig = properties.getRpcConfig();
        ReinforceOptions defaultOptions = rpcConfig.get("default");
        dispatch.entrySet().stream().forEach(e -> {
            Method method = e.getKey();
            InvocationHandlerFactory.MethodHandler methodHandler = e.getValue();
            ReinforceOptions.Options options = getOptions(rpcConfig, method, defaultOptions);
            if (options != null){
                FieldUtils.setFieldValue(methodHandler, "options", options);
            }
        });
    }


    private boolean isAllowedRetry(Method method){
        RpcInfo annotation = method.getAnnotation(RpcInfo.class);
        //如果从方法上没找到
        if (annotation == null){
            //从父类头上获取
            annotation = method.getDeclaringClass().getAnnotation(RpcInfo.class);
        }
        //如果服务提供方没有提供注解 默认不允许重试
        return annotation == null ? false : annotation.isAllowedRetry();
    }

    /**
     * 获取 接口 RPC 配置
     * @param rpcConfig
     * @param method
     * @param defaultOptions
     * @return
     */
    private ReinforceOptions.Options getOptions(Map<String, ReinforceOptions> rpcConfig,Method method,ReinforceOptions defaultOptions){
        /*
         *这里是开始拼接配置的 Key
         * 服务消费者方配置：
         *  feign:
         *    client:
         *      rpcConfig:
         *        #单个方法维度的配置
         *        #接口名称 + # + 方法名称 + (参数类型, 参数类型...)
         *        #包名 + 接口名称 + # + 方法名称 + (参数类型, 参数类型...) com.huihuang.service.MemberService#getUsers(String)
         *        MemberService#getUsers(String):
         *          connectTimeout: 1 #连接超时
         *          connectTimeoutUnit: SECONDS #超时时间单位
         *          readTimeout: 1 #读取超时
         *          readTimeoutUnit: SECONDS
         *          followRedirects: false #是否允许重定向
         *          maxAutoRetries: 2 # 对当前实例的重试次数
         *          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
         *        #按接口维度配置 优先级低于 单个方法维度的配置
         *        #接口名称
         *        #包名 + 接口名称 com.huihuang.service.MemberService
         *        MemberService:
         *          connectTimeout: 1 #连接超时
         *          connectTimeoutUnit: SECONDS #超时时间单位
         *          readTimeout: 1 #读取超时
         *          readTimeoutUnit: SECONDS
         *          followRedirects: false #是否允许重定向
         *          maxAutoRetries: 2 # 对当前实例的重试次数
         *          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
         *        #全局维度配置 优先级低于 单个方法维度 和 接口维度 的配置
         *        default:
         *          connectTimeout: 1 #连接超时
         *          connectTimeoutUnit: SECONDS #超时时间单位
         *          readTimeout: 1 #读取超时
         *          readTimeoutUnit: SECONDS
         *          followRedirects: false #是否允许重定向
         *          maxAutoRetries: 2 # 对当前实例的重试次数
         *          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
         */
        Class<?> declaringClass = method.getDeclaringClass();
        String simpleName = declaringClass.getSimpleName();
        StringBuilder configKey = new StringBuilder(simpleName);
        String methodName = method.getName();
        configKey.append(methodName);
        //获取方法级别 配置 start
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> c : parameterTypes){
            configKey.append(c.getSimpleName());
        }
        ReinforceOptions reinforceOptions = rpcConfig.get(configKey.toString());
        if (reinforceOptions != null){
            if (reinforceOptions.isRetry()){
                return reinforceOptions.options(isAllowedRetry(method));
            }else {
                return reinforceOptions.options();
            }
        }
        String name = declaringClass.getName();
        configKey = new StringBuilder(name);
        configKey.append(methodName);
        for (Class<?> c : parameterTypes){
            configKey.append(c.getSimpleName());
        }
        reinforceOptions = rpcConfig.get(configKey.toString());
        if (reinforceOptions != null){
            if (reinforceOptions.isRetry()){
                return reinforceOptions.options(isAllowedRetry(method));
            }else {
                return reinforceOptions.options();
            }
        }
        RpcInfo annotation = method.getAnnotation(RpcInfo.class);
        if (annotation != null){
            return ReinforceOptions.options(annotation);
        }
        //获取方法级别 end

        //获取接口级别 start
        reinforceOptions = rpcConfig.get(simpleName);
        if (reinforceOptions != null){
            if (reinforceOptions.isRetry()){
                return reinforceOptions.options(isAllowedRetry(method));
            }else {
                return reinforceOptions.options();
            }
        }
        reinforceOptions = rpcConfig.get(name);
        if (reinforceOptions != null){
            if (reinforceOptions.isRetry()){
                return reinforceOptions.options(isAllowedRetry(method));
            }else {
                return reinforceOptions.options();
            }
        }
        annotation = declaringClass.getAnnotation(RpcInfo.class);
        if (annotation != null){
            return ReinforceOptions.options(annotation);
        }
        //获取接口级别 end

        //获取全局 说明没有获取到 服务提供者注解 默认不让重试
        if (defaultOptions != null){
            return defaultOptions.options();
        }
        return null;
    }
}
