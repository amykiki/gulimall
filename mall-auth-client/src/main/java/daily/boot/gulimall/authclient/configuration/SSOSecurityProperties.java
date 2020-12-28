package daily.boot.gulimall.authclient.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gulimall.sso")
public class SSOSecurityProperties {
    private String authServer;
    private String requestMatchers;
    private String logoutUrl;
    
    public String getAuthServer() {
        return authServer;
    }
    
    public void setAuthServer(String authServer) {
        this.authServer = authServer;
    }
    
    public String getRequestMatchers() {
        return requestMatchers;
    }
    
    public void setRequestMatchers(String requestMatchers) {
        this.requestMatchers = requestMatchers;
    }
    
    public String getLogoutUrl() {
        return logoutUrl;
    }
    
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
