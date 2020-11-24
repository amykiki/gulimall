package daily.boot.gulimall.common.httpclient;

public enum Scheme {
    HTTP("HTTP://"),
    HTTPS("HTTPS://"),
    WEBSOCKET("WS://");
    String value;
    
    Scheme(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
