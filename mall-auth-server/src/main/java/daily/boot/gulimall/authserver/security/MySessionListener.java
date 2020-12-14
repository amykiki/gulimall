package daily.boot.gulimall.authserver.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
@Slf4j
/**
 * 这个类只是实例，暂时不起作用，使用spring Security 自带的HttpSessionEventPublisher代替
 */
public class MySessionListener implements HttpSessionListener {
    
    private ApplicationContext getContext(ServletContext servletContext) {
        return SecurityWebApplicationContextUtils.findRequiredWebApplicationContext(servletContext);
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(se.getSession());
        if (log.isDebugEnabled()) {
            log.debug("Publishing event: " + e);
        }
    
        getContext(se.getSession().getServletContext()).publishEvent(e);
    }
}
