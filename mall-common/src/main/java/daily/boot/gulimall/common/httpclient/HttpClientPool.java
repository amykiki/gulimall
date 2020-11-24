package daily.boot.gulimall.common.httpclient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 参考
 * https://www.cnblogs.com/mumuxinfei/p/9122421.html
 * https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/HttpSyncClient.java
 *
 */
public class HttpClientPool {
    
    private Logger logger = LoggerFactory.getLogger(HttpClientPool.class);
    
    private PoolingHttpClientConnectionManager pcm = null;
    private CloseableHttpClient httpClient = null;
    private IdleConnectionMonitorThread idleThread = null;
    
    private ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
        BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement header = it.nextElement();
            String param = header.getName();
            String value = header.getValue();
            if (Objects.nonNull(value) && param.equalsIgnoreCase("timeout")) {
                return Long.parseLong(value) * 1000;
            }
        }
        //默认keep-alive时长60s
        return 60 * 1000;
    };
    
    //连接池最大连接数
    private final int maxTotal;
    //连接池按route配置的最大连接数
    private final int maxPerRoute;
    
    //tcp connect超时时间
    private final int connectTimeout;
    //从连接池获取连接的超时时间
    private final int connectRequestTimeout;
    //tcp io的数据读写超时时间
    private final int socketTimeout;
    
    public HttpClientPool(HttpClientPoolConfig config) {
        this.maxTotal = config.getMaxTotal();
        this.maxPerRoute = config.getMaxPerRoute();
        this.connectTimeout = config.getConnectTimeout();
        this.connectRequestTimeout = config.getConnectRequestTimeout();
        this.socketTimeout = config.getSocketTimeout();
    
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HttpClient连接池初始化失败");
        }
    }
    
    private void init() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //设置SSL
        SSLContext sslContext = SSLContexts.custom()
                                           .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                                           .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
    
        this.pcm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        this.pcm.setMaxTotal(this.maxTotal);
        this.pcm.setDefaultMaxPerRoute(this.maxPerRoute);
    
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setConnectionRequestTimeout(this.connectRequestTimeout)
                .setSocketTimeout(this.socketTimeout)
                .build();
        this.httpClient = HttpClients.custom()
                                     .setConnectionManager(this.pcm)
                                     .setDefaultRequestConfig(requestConfig)
                                     .setKeepAliveStrategy(keepAliveStrategy)
                                     .build();
        this.idleThread = new IdleConnectionMonitorThread(this.pcm);
        this.idleThread.start();
    }
    
    public String doGet(String url) {
        return this.doGet(url, null, null);
    }
    
    public String doGet(String url, Map<String, Object> params) {
        return this.doGet(url, null, params);
    }
    
    public String doGet(String url, Map<String, String> headers, Map<String, Object> params) {
        //构建GET请求头
        String apiUrl = getUrlWithParams(url, params);
        HttpGet httpGet = new HttpGet(apiUrl);
        
        //设置header信息
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpGet::addHeader);
        }
    
        try {
            return executeRequestStr(httpGet);
        } catch (IOException e) {
            logger.warn("HttpGet请求异常:{}", apiUrl, e);
        }
        return null;
    }
    
    public String doPost(String apiUrl, Map<String, Object> params) {
        return this.doPost(apiUrl, null, params);
    }
    public String doPost(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(apiUrl);
        
        //配置请求headers
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpPost::addHeader);
        }
        
        //配置请求参数
        if (!CollectionUtils.isEmpty(params)) {
            HttpEntity reqEntity = getUrlEncodedFormEntity(params);
            httpPost.setEntity(reqEntity);
        }
        try {
            return executeRequestStr(httpPost);
        } catch (IOException e) {
            logger.warn("HttpPost请求异常:{}-{}", apiUrl, httpPost.getEntity().toString(), e);
        }
        return null;
    }
    
    public HttpClientResp sendRequest(HttpClientReq req) {
        HttpUriRequest httpUriRequest = buildRequest(req);
        try {
            return executeRequest(httpUriRequest);
        } catch (IOException e) {
            logger.warn("HttpClientReq请求异常:{}", req, e);
        }
        return null;
    }
    
    private HttpClientResp executeRequest(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }
            HttpClientResp clientResp = new HttpClientResp(response.getStatusLine().getStatusCode());
            
            //设置header
            Map<String, List<String>> headers = Arrays.stream(response.getAllHeaders()).collect(
                    Collectors.toMap(Header::getName,
                                     h -> new ArrayList<>(Collections.singletonList(h.getValue())),
                                     (oList, nList) -> {
                                         oList.addAll(nList);
                                         return oList;
                                     }));
            
            clientResp.setHeaders(headers);
            //设置message
            clientResp.setMessage(response.getStatusLine().getReasonPhrase());
            //设置返回数据
            if (response.getEntity() != null) {
                //获取content Type
                Header contentType = response.getEntity().getContentType();
                if (Objects.nonNull(contentType)) {
                    clientResp.setContentType(contentType.getValue());
                } else {
                    clientResp.setContentType(HttpConstant.CONTENT_TYPE_TEXT);
                }
                
                //获取内容
                clientResp.setContent(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
                
            } else {
                String contentTypeStr = clientResp.getFirstHeaderValue(HttpConstant.HTTP_HEADER_CONTENT_TYPE);
                if (Objects.isNull(contentTypeStr)) {
                    contentTypeStr = HttpConstant.CONTENT_TYPE_TEXT;
                }
                clientResp.setContentType(contentTypeStr);
            }
            
            return clientResp;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String executeRequestStr(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }
    
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    return EntityUtils.toString(entityRes, "UTF-8");
                }
            }
            return null;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private HttpEntity getUrlEncodedFormEntity(Map<String, Object> params) {
        if (!CollectionUtils.isEmpty(params)) {
            ArrayList<NameValuePair> pairList = new ArrayList<>(params.size());
            params.forEach((key, val) -> {
                BasicNameValuePair pair = new BasicNameValuePair(key, val.toString());
                pairList.add(pair);
            });
            return new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8);
        }
        return null;
    }
    private String getUrlWithParams(String url, Map<String, Object> params) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(url);
        if (!CollectionUtils.isEmpty(params)) {
            String encodeParams = params.entrySet().stream().map(entry -> {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                try {
                    String encodeVal = URLEncoder.encode(value, "UTF-8");
                    return key + "=" + encodeVal;
                } catch (UnsupportedEncodingException e) {
                    logger.warn("HttpClientPool--URLEncode参数失败:{}:{}", key, value);
                }
                return "";
            }).collect(Collectors.joining("&"));
            if (StringUtils.isNotBlank(encodeParams)) {
                encodeParams = encodeParams.trim();
                sb.append("?").append(encodeParams);
            }
            
        }
        return sb.toString();
    }
    
    private HttpUriRequest buildRequest(HttpClientReq req) {
        RequestBuilder reqBuilder = RequestBuilder.create(req.getHttpMethod().getValue());
        try {
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setCharset(Consts.UTF_8);
            uriBuilder.setScheme(req.getScheme().name());
            uriBuilder.setHost(req.getHost());
            uriBuilder.setPath(req.getPath());
            if (!CollectionUtils.isEmpty(req.getQueries())) {
                req.getQueries().forEach((key, value) -> {
                    value.forEach(val -> {
                        uriBuilder.addParameter(key, val);
                    });
                });
            }
            reqBuilder.setUri(uriBuilder.build());
        } catch (URISyntaxException e) {
            logger.error("解析HttpClientReq参数异常:{}", req);
            throw new RuntimeException("HTTP URL参数解析异常", e);
        }
        EntityBuilder bodyBuilder = EntityBuilder.create();
        bodyBuilder.setContentType(ContentType.parse(req.getHttpMethod().getRequestContentType()));
        if (!CollectionUtils.isEmpty(req.getFormParams())) {
            List<NameValuePair> paramList = new ArrayList<>();
            req.getFormParams().forEach((key, vals) -> {
                vals.forEach(val -> {
                    paramList.add(new BasicNameValuePair(key, val));
                });
            });
            bodyBuilder.setParameters(paramList);
            reqBuilder.setEntity(bodyBuilder.build());
        } else if (req.getBody() != null && req.getBody().length > 0) {
            bodyBuilder.setBinary(req.getBody());
            reqBuilder.setEntity(bodyBuilder.build());
        }
    
        if (!CollectionUtils.isEmpty(req.getHeaders())) {
            req.getHeaders().forEach((key, vals) -> {
                vals.forEach(val -> {
                    reqBuilder.addHeader(key, val);
                });
            });
        }
        
        return reqBuilder.build();
    }
    
    
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (pcm != null) {
            pcm.close();
        }
        idleThread.shutdown();
    }
    
    private class IdleConnectionMonitorThread extends Thread {
        private final HttpClientConnectionManager connMgr;
        private volatile boolean exitFlag = false;
    
        IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
            setDaemon(true);
        }
    
        @Override
        public void run() {
            while (!this.exitFlag) {
                synchronized (this) {
                    try {
                        this.wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //关闭失效连接
                connMgr.closeExpiredConnections();
                //可选，关闭80秒内不活动的连接
                connMgr.closeIdleConnections(80, TimeUnit.SECONDS);
            }
        }
        
        void shutdown() {
            this.exitFlag = true;
            synchronized (this) {
                notify();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        HttpClientPoolConfig config = new HttpClientPoolConfig();
        config.setMaxPerRoute(10);
        config.setMaxTotal(20);
    
        HttpClientPool pool = new HttpClientPool(config);
        //executeStrTest(pool);
        executeReqTest(pool);
        
        
        //ExecutorService executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        //for (int i = 0; i < 20; i++) {
        //    String url = getUrl + "?index=" + i;
        //    try {
        //        executor.execute(() -> pool.doGet(url));
        //    } catch (Exception e) {
        //        System.out.println(url + "线程池异常");
        //        e.printStackTrace();
        //    }
        //}
        //executor.shutdown();
        //while (!executor.isTerminated()){Thread.sleep(100);}
        //System.out.println("Executor is shutdown!");
        //Thread.sleep(45 * 1000L);
        //s = pool.doGet(getUrl, data);
        //System.out.println(s);
        //Thread.sleep(30 * 1000L);
        //s = pool.doPost(postUrl, data);
        //System.out.println(s);
    }
    
    private static void executeStrTest(HttpClientPool pool) {
        String baseUrl = "https://httpbin.org/";
        String getUrl = "https://httpbin.org/get";
        String postUrl = "https://httpbin.org/post";
        String delayUrl = "https://httpbin.org/delay/2";
        String s = null;
        Map<String, Object> data = new HashMap<>();
        data.put("a", "abc");
        data.put("b", "book");
        //String s = pool.doGet(getUrl, data);
        //System.out.println(s);
        //s = pool.doPost(postUrl, data);
        //System.out.println(s);
        //超时
        s = pool.doGet(delayUrl);
        System.out.println(s);
    }
    
    private static void executeReqTest(HttpClientPool pool) {
        String host = "httpbin.org";
        String path = "get";
        HttpClientReq req = new HttpClientReq(HttpMethod.GET, Scheme.HTTPS, host, path);
        req.addHeader(HttpConstant.HTTP_HEADER_CONTENT_TYPE, req.getHttpMethod().getAcceptContentType());
        req.addQuery("name", "amy");
        req.addQuery("name", "周周");
        req.addHeader("t-n1", "n1");
        req.addHeader("t-n2", "n2");
        req.addHeader("t-n1", "n1-1");
    
        HttpClientResp resp = pool.sendRequest(req);
        System.out.println(resp);
    }
    
}
