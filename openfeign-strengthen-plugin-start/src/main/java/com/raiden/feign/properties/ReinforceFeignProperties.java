package com.raiden.feign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 17:55 2021/11/6
 * @Modified By:
 */
@ConfigurationProperties(prefix = "feign.client")
public class ReinforceFeignProperties {

    private Map<String, ReinforceOptions> rpcConfig;

    public ReinforceFeignProperties(){
        rpcConfig = new HashMap<>();
    }

    public Map<String, ReinforceOptions> getRpcConfig() {
        return rpcConfig;
    }

    public void setRpcConfig(Map<String, ReinforceOptions> rpcConfig) {
        this.rpcConfig = rpcConfig;
    }
}
