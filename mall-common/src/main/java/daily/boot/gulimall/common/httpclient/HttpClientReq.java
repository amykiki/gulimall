package daily.boot.gulimall.common.httpclient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HttpClientReq extends BaseHttpMessage {
    private Scheme scheme;
    private HttpMethod httpMethod;
    private String host;
    private String path;
    private String url;
    
    public HttpClientReq(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        this.scheme = Scheme.HTTP;
    }
    
    public HttpClientReq(HttpMethod httpMethod, Scheme scheme) {
        this.httpMethod = httpMethod;
        this.scheme = scheme;
    }
    
    public HttpClientReq(HttpMethod httpMethod, String host, String path) {
        this.httpMethod = httpMethod;
        this.host = host;
        this.path = path;
        this.scheme = Scheme.HTTP;
    }
    
    public HttpClientReq(HttpMethod httpMethod, Scheme scheme, String host, String path) {
        this.httpMethod = httpMethod;
        this.scheme = scheme;
        this.host = host;
        this.path = path;
    }
    
    private Map<String, String> pathParams = new HashMap<>();
    private Map<String, List<String>> queries = new HashMap<>();
    private Map<String, List<String>> formParams = new HashMap<>();
    
    public void addQuery(String name, String value) {
        addParam(name, value, queries);
    }
    public void addParam(String name, String value) {
        addParam(name, value, formParams);
    }
    public Scheme getScheme() {
        return scheme;
    }
    
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }
    
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
    
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Map<String, String> getPathParams() {
        return pathParams;
    }
    
    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }
    
    public Map<String, List<String>> getQueries() {
        return queries;
    }
    
    public void setQueries(Map<String, List<String>> queries) {
        this.queries = queries;
    }
    
    public Map<String, List<String>> getFormParams() {
        return formParams;
    }
    
    public void setFormParams(Map<String, List<String>> formParams) {
        this.formParams = formParams;
    }
    
    
    @Override
    public String toString() {
        return "HttpClientReq{" +
               "scheme=" + scheme +
               ", httpMethod=" + httpMethod +
               ", host='" + host + '\'' +
               ", path='" + path + '\'' +
               ", url='" + url + '\'' +
               ", pathParams=" + pathParams +
               ", querys=" + queries +
               ", formParams=" + formParams +
               "} " + super.toString();
    }
}
