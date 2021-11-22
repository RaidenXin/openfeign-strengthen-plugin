# openfeign-strengthen-plugin
# 顾名思义这是一个关于openfeign客户端的强化插件
本插件的作用在于 补全openfeign客户端在使用的默认代理和 Sentinel 代理
的过程中无法自定义单个接口或者单个方法级别的超时时间与重试的插件

本插件的优先级为 服务消费者method配置 > 服务消费者interface级配置 > 服务消费者全局配置 > 服务提供者
 
 服务提供方 maven 引用:
 
                <dependency>
                    <groupId>org.example</groupId>
                    <artifactId>openfeign-strengthen-plugin-annotation</artifactId>
                    <version>1.0-SNAPSHOT</version>
                </dependency>
 
 java 代码书写方式(服务提供方使用注解形式):
 
                /**
                 * 注解放在 interface 上,则该 interface 下所有的方法 共享该配置
                 */
                @RpcInfo(connectTimeout = 2,connectTimeoutUnit = TimeUnit.SECONDS, readTimeout = 1, readTimeoutUnit = TimeUnit.SECONDS, followRedirects = false)
                public interface MemberService {
                
                    /**
                     * 注解放在 method 上
                     * 则该 method  RPC调用 时使用该配置。
                     * 方法上的配置优先级大于 interface 级别,
                     * 同时存在时,以 method 上的为准
                     * connectTimeout 连接超时时间
                     * connectTimeoutUnit 连接超时时间的单位 默认为毫秒
                     * readTimeout 读取超时时间
                     * readTimeoutUnit 读取超时时间单位 默认为毫秒
                     * followRedirects 是否重定向 默认为否
                     * maxAutoRetriesNextServer 切换实例的重试次数 默认为 0 次 禁止重试
                     * maxAutoRetries 对当前实例的重试次数 默认为 0 次 禁止重试
                     * isAllowedRetry 是否允许重试 默认为 false 不允许
                     */
                    @RequestMapping("/getUsers")
                    @RpcInfo(connectTimeout = 2,connectTimeoutUnit = TimeUnit.SECONDS, 
                            readTimeout = 1, readTimeoutUnit = TimeUnit.SECONDS, followRedirects = false, 
                            maxAutoRetriesNextServer = 1, maxAutoRetries = 1, isAllowedRetry = true)
                    List<User> getUsers(String name);
                
                    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
                    @RpcInfo(connectTimeout = 5,connectTimeoutUnit = TimeUnit.SECONDS, readTimeout = 2, readTimeoutUnit = TimeUnit.SECONDS)
                    String handleFileUpload(@RequestPart(value = "file") MultipartFile file);
                }




 服务消费者方使用 application.yml 配置的形式使用,并且服务消费者超时时间配置优先级高于服务提供方配置
 
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
