package daily.boot.gulimall.common.httpclient;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gulimall.httpclient")
public class HttpClientPoolConfig {
    private static final int DEFAULT_POOL_MAX_TOTAL = 400;
    private static final int DEFAULT_POOL_MAX_PER_ROUTE = 200;
    //连接超时时间
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    //从连接池中获取连接超时时间
    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 500;
    //socket数据传输超时时间
    private static final int DEFAULT_SOCKET_TIMEOUT = 2000;
    
    //连接池最大连接数
    private int maxTotal = DEFAULT_POOL_MAX_TOTAL;
    //连接池按route配置的最大连接数
    private int maxPerRoute = DEFAULT_POOL_MAX_PER_ROUTE;
    
    //tcp connect超时时间
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    //从连接池获取连接的超时时间
    private int connectRequestTimeout = DEFAULT_CONNECT_REQUEST_TIMEOUT;
    //tcp io的数据读写超时时间
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    
    public int getMaxTotal() {
        return maxTotal;
    }
    
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public int getMaxPerRoute() {
        return maxPerRoute;
    }
    
    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }
    
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }
    
    public void setConnectRequestTimeout(int connectRequestTimeout) {
        this.connectRequestTimeout = connectRequestTimeout;
    }
    
    public int getSocketTimeout() {
        return socketTimeout;
    }
    
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
