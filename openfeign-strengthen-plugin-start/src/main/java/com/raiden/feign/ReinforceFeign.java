package com.raiden.feign;

import com.raiden.feign.properties.ReinforceFeignProperties;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 17:04 2021/11/6
 * @Modified By:
 */
public final class ReinforceFeign {
    private ReinforceFeign(){}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Feign.Builder implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        public Feign build() {
            ReinforceFeignProperties properties = applicationContext.getBean(ReinforceFeignProperties.class);
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    return new ReinforceFeignInvocationHandler(target, dispatch, properties);
                }
            });
            return super.build();
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }
}
