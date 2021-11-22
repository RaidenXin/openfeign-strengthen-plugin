# openfeign-strengthen-plugin
# 顾名思义这是一个关于openfeign客户端的强化插件
本插件的作用在于 补全openfeign客户端在使用的默认代理和 Sentinel 代理
的过程中无法自定义单个接口或者单个方法级别的超时时间与重试的插件

服务消费者配置:
 服务消费者方配置：
  feign:
    client:
      rpcConfig:
        #单个方法维度的配置
        #接口名称 + # + 方法名称 + (参数类型, 参数类型...)
        #包名 + 接口名称 + # + 方法名称 + (参数类型, 参数类型...) com.huihuang.service.MemberService#getUsers(String)
        MemberService#getUsers(String):
          connectTimeout: 1 #连接超时
          connectTimeoutUnit: SECONDS #超时时间单位
          readTimeout: 1 #读取超时
          readTimeoutUnit: SECONDS
          followRedirects: false #是否允许重定向
          maxAutoRetries: 2 # 对当前实例的重试次数
          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
        #按接口维度配置 优先级低于 单个方法维度的配置
        #接口名称
        #包名 + 接口名称 com.huihuang.service.MemberService
        MemberService:
          connectTimeout: 1 #连接超时
          connectTimeoutUnit: SECONDS #超时时间单位
          readTimeout: 1 #读取超时
          readTimeoutUnit: SECONDS
          followRedirects: false #是否允许重定向
          maxAutoRetries: 2 # 对当前实例的重试次数
          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
        #全局维度配置 优先级低于 单个方法维度 和 接口维度 的配置
        default:
          connectTimeout: 1 #连接超时
          connectTimeoutUnit: SECONDS #超时时间单位
          readTimeout: 1 #读取超时
          readTimeoutUnit: SECONDS
          followRedirects: false #是否允许重定向
          maxAutoRetries: 2 # 对当前实例的重试次数
          maxAutoRetriesNextServer: 1 # 切换实例的重试次数
