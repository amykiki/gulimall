package daily.boot.gulimall.common.httpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseHttpMessage {
    protected byte[] body;
    protected Map<String, List<String>> headers = new HashMap<>();
    
    public void addHeader(String name, String value) {
        name = name.trim().toLowerCase();
        addParam(name, value, headers);
    }
    
    protected void addParam(String name, String value, Map<String, List<String>> map) {
        if (map.containsKey(name)) {
            map.get(name).add(value);
        }else {
            List<String> values = new ArrayList<>();
            values.add(value == null ? "" : value.trim());
            map.put(name, values);
        }
        
    }
    
    public String getFirstHeaderValue(String name) {
        if (headers.containsKey(name) && headers.get(name).size() > 0) {
            return headers.get(name).get(0);
        }
        return null;
    }
    
    public byte[] getBody() {
        return body;
    }
    
    public void setBody(byte[] body) {
        this.body = body;
    }
    
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
    
    
}
