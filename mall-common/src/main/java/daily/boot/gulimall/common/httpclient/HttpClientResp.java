package daily.boot.gulimall.common.httpclient;

public class HttpClientResp extends BaseHttpMessage {
    private int code;
    private String message;
    private String contentType;
    private String content;
    
    public HttpClientResp(int code) {
        this.code = code;
    }
    
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "HttpClientResp{" +
               "code=" + code +
               ", message='" + message + '\'' +
               ", contentType='" + contentType + '\'' +
               ", content='" + content + '\'' +
               ", headers=" + headers +
               "} ";
    }
}
