package daily.boot.gulimall.authserver.singlelogout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 参考 过滤器（Filter）与拦截器（Interceptor )区别(https://www.cnblogs.com/jing99/p/11146624.html)
 */

public class OAuth2SsoFilter extends OncePerRequestFilter {
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("before");
        filterChain.doFilter(request, response);
        HttpSession session = request.getSession(false);
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<SessionInformation> amykiki = sessionRegistry.getAllSessions("amykiki", false);
        System.out.println("after");
    }
    //@Override
    //public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    //    //super.postHandle(request, response, handler, modelAndView);
    //    HttpSession session = request.getSession(false);
    //    if (handler instanceof HandlerMethod) {
    //        HandlerMethod handlerMethod = (HandlerMethod) handler;
    //        System.out.printf("当前拦截的方法为：%s\n", handlerMethod.getMethod().getName());
    //        System.out.printf("当前拦截的方法参数长度为：%s\n", handlerMethod.getMethod().getParameters().length);
    //        System.out.printf("当前拦截的方法为：%s\n", handlerMethod.getBean().getClass().getName());
    //        System.out.println("开始拦截---------");
    //        String uri = request.getRequestURI();
    //        System.out.println("拦截的uri：" + uri);
    //
    //    }
    //}
}
