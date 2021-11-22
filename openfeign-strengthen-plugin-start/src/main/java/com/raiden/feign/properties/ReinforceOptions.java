package com.raiden.feign.properties;

import com.raiden.feign.annotation.RpcInfo;
import feign.Request;

import java.util.concurrent.TimeUnit;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 18:44 2021/11/6
 * @Modified By:
 */
public class ReinforceOptions {

    private long connectTimeout;
    private TimeUnit connectTimeoutUnit;
    private long readTimeout;
    private TimeUnit readTimeoutUnit;
    private boolean followRedirects;

    private int maxAutoRetriesNextServer = 0;
    private int maxAutoRetries = 0;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public TimeUnit getConnectTimeoutUnit() {
        return connectTimeoutUnit;
    }

    public void setConnectTimeoutUnit(TimeUnit connectTimeoutUnit) {
        this.connectTimeoutUnit = connectTimeoutUnit;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public TimeUnit getReadTimeoutUnit() {
        return readTimeoutUnit;
    }

    public void setReadTimeoutUnit(TimeUnit readTimeoutUnit) {
        this.readTimeoutUnit = readTimeoutUnit;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public int getMaxAutoRetriesNextServer() {
        return maxAutoRetriesNextServer;
    }

    public void setMaxAutoRetriesNextServer(int maxAutoRetriesNextServer) {
        this.maxAutoRetriesNextServer = maxAutoRetriesNextServer;
    }

    public int getMaxAutoRetries() {
        return maxAutoRetries;
    }

    public void setMaxAutoRetries(int maxAutoRetries) {
        this.maxAutoRetries = maxAutoRetries;
    }

    public boolean isRetry(){
        return this.maxAutoRetries > 0 || this.maxAutoRetriesNextServer > 0;
    }

    public static class Options extends Request.Options{

        private int maxAutoRetriesNextServer;
        private int maxAutoRetries;
        private boolean isAllowedRetry;

        public Options(ReinforceOptions options){
            super(options.connectTimeout, options.connectTimeoutUnit, options.readTimeout, options.readTimeoutUnit, options.followRedirects);
            this.maxAutoRetries = options.maxAutoRetries;
            this.maxAutoRetriesNextServer = options.maxAutoRetriesNextServer;
        }

        public Options(RpcInfo info){
            super(info.connectTimeout(), info.connectTimeoutUnit(), info.readTimeout(), info.readTimeoutUnit(), info.followRedirects());
            this.maxAutoRetries = info.maxAutoRetries();
            this.maxAutoRetriesNextServer = info.maxAutoRetriesNextServer();
            this.isAllowedRetry = info.isAllowedRetry();
        }

        public int getMaxAutoRetriesNextServer() {
            return maxAutoRetriesNextServer;
        }

        public void setMaxAutoRetriesNextServer(int maxAutoRetriesNextServer) {
            this.maxAutoRetriesNextServer = maxAutoRetriesNextServer;
        }

        public int getMaxAutoRetries() {
            return maxAutoRetries;
        }

        public Options setMaxAutoRetries(int maxAutoRetries) {
            this.maxAutoRetries = maxAutoRetries;
            return this;
        }

        public boolean isAllowedRetry() {
            return isAllowedRetry;
        }

        public Options setAllowedRetry(boolean allowedRetry) {
            isAllowedRetry = allowedRetry;
            return this;
        }
    }

    public Options options(boolean isAllowedRetry){
        return new Options(this).setAllowedRetry(isAllowedRetry);
    }

    public Options options(){
        return new Options(this);
    }

    public static Options options(RpcInfo info){
        return new Options(info);
    }
}
