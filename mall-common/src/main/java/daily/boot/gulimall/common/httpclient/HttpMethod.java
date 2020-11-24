package daily.boot.gulimall.common.httpclient;

public enum HttpMethod {
    
    
    /**
     * httpGet
     */
    GET("GET", HttpConstant.CONTENT_TYPE_FORM, HttpConstant.CONTENT_TYPE_JSON),
    
    /**
     * httpPost with form
     */
    POST_FORM("POST", HttpConstant.CONTENT_TYPE_FORM, HttpConstant.CONTENT_TYPE_JSON),
    
    /**
     * httpPost with binary body
     */
    POST_BODY("POST", HttpConstant.CONTENT_TYPE_STREAM, HttpConstant.CONTENT_TYPE_JSON),
    
    /**
     * httpPut with form
     */
    PUT_FORM("PUT", HttpConstant.CONTENT_TYPE_FORM, HttpConstant.CONTENT_TYPE_JSON),
    
    /**
     * httpPut with binary body
     */
    PUT_BODY("PUT", HttpConstant.CONTENT_TYPE_STREAM, HttpConstant.CONTENT_TYPE_JSON),
    /**
     * httpDelete
     */
    DELETE("DELETE", HttpConstant.CONTENT_TYPE_FORM, HttpConstant.CONTENT_TYPE_JSON);
    private String value;
    private String requestContentType;
    private String acceptContentType;
    
    HttpMethod(String value, String requestContentType, String acceptContentType) {
        this.value = value;
        this.requestContentType = requestContentType;
        this.acceptContentType = acceptContentType;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getRequestContentType() {
        return requestContentType;
    }
    
    public String getAcceptContentType() {
        return acceptContentType;
    }
}
