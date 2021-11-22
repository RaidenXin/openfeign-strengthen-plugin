package com.raiden.feign.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.SphU;
import com.raiden.feign.ReinforceFeign;
import com.raiden.feign.ReinforceSentinelFeign;
import com.raiden.feign.properties.ReinforceFeignProperties;
import feign.Client;
import feign.Feign;
import feign.Feign.Builder;
import feign.Retryer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.ReinforceLoabBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration(
    proxyBeanMethods = false
)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@EnableConfigurationProperties(ReinforceFeignProperties.class)
public class ReinforceFeignAutoConfiguration {
    public ReinforceFeignAutoConfiguration() {
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        name = {"feign.sentinel.enabled"}
    )
    @ConditionalOnClass({SphU.class, Feign.class})
    public Builder feignSentinelBuilder() {
        return ReinforceSentinelFeign.builder();
    }


    @Bean
    @ConditionalOnMissingBean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public Feign.Builder feignBuilder(Retryer retryer) {
        return ReinforceFeign.builder().retryer(retryer);
    }

    @Bean
    @ConditionalOnMissingBean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) {
        return new ReinforceLoabBalancerFeignClient(new Client.Default(null, null), cachingFactory,
                clientFactory);
    }
}
