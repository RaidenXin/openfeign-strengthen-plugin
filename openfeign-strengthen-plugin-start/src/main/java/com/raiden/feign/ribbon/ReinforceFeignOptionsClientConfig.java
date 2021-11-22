package com.raiden.feign.ribbon;

import com.raiden.feign.properties.ReinforceOptions;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import feign.Request;

public class ReinforceFeignOptionsClientConfig extends DefaultClientConfigImpl {

		public ReinforceFeignOptionsClientConfig(Request.Options options) {
		    if (options == null){
		        throw new NullPointerException("The options parameter cannot be Null!");
            }
		    //设置超时时间
			setProperty(CommonClientConfigKey.ConnectTimeout, options.connectTimeoutMillis());
			setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
			//设置重试机制 如果是不允许重试的接口 就不设置
			if (options instanceof ReinforceOptions.Options && ((ReinforceOptions.Options) options).isAllowedRetry()){
                setProperty(CommonClientConfigKey.MaxAutoRetries, ((ReinforceOptions.Options) options).getMaxAutoRetries());
                setProperty(CommonClientConfigKey.MaxAutoRetriesNextServer, ((ReinforceOptions.Options) options).getMaxAutoRetriesNextServer());
                setProperty(CommonClientConfigKey.OkToRetryOnAllOperations, true);
            }
		}

		@Override
		public void loadProperties(String clientName) {

		}

		@Override
		public void loadDefaultValues() {

		}

	}
