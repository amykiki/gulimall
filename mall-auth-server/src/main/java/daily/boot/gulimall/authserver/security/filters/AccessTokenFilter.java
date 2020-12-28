package daily.boot.gulimall.authserver.security.filters;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import daily.boot.gulimall.authserver.security.OAuth2SessionRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * /oauth/token 过滤器，用于把授权accessToken和用户登录session绑定
 * 参考
 * springboot 使用过滤器获取response内容保存接口访问日志 https://blog.csdn.net/a582127421/article/details/78321888
 * 解决HttpServletRequest 流数据不可重复读 https://www.cnblogs.com/Sinte-Beuve/p/13260249.html
 * Servlet输入流输出流只能消费一次的问题 https://www.jianshu.com/p/9b717551ee44
 */
@Slf4j
@Component
public class AccessTokenFilter extends OncePerRequestFilter {
    
    @Autowired
    private OAuth2SessionRelationService oAuth2SessionRelationService;
    @Autowired
    @Qualifier("jdbcTokenStore")
    private TokenStore tokenStore;
    private ObjectMapper objectMapper;
    private BasicAuthenticationConverter authenticationConverter;
    
    //public AccessTokenFilter(OAuth2SessionRelationService oAuth2SessionRelationService, TokenStore tokenStore) {
    //    this.oAuth2SessionRelationService = oAuth2SessionRelationService;
    //    this.tokenStore = tokenStore;
    //}
    
    
    @Override
    protected void initFilterBean() throws ServletException {
        authenticationConverter = new BasicAuthenticationConverter();
        objectMapper = new ObjectMapper();
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //封装response，以便在请求结束后，能读取response内容
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);
        //获取code
        String code = request.getParameter("code");
        if (Objects.isNull(code)) {
            responseWrapper.copyBodyToResponse();
            return;
        }
        
        //获取返回内容，从中取出accessToken存入session
        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
            String tokenStr = new String(contentAsByteArray, StandardCharsets.UTF_8);
            oAuth2AccessToken = objectMapper.readValue(tokenStr, OAuth2AccessToken.class);
        } catch (Exception e) {
            log.error("AccessTokenFilter从response中取出accessToken失败");
        }
        if (Objects.isNull(oAuth2AccessToken) || Objects.isNull(oAuth2AccessToken.getValue())) {
            responseWrapper.copyBodyToResponse();
            return;
        }
        //根据oauth2AccessToken找到authentication，找到登录用户名
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken.getValue());
        if (Objects.isNull(oAuth2Authentication)) {
            responseWrapper.copyBodyToResponse();
            return;
        }
        String username = oAuth2Authentication.getName();
        
        //从header中获取clientId
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = authenticationConverter.convert(request);
        if (Objects.isNull(usernamePasswordAuthenticationToken)) {
            responseWrapper.copyBodyToResponse();
            return;
        }
        String clientId = (String) usernamePasswordAuthenticationToken.getPrincipal();
        //根据code和clientId查找对应session，如果找到，则把accessToken存入sessoin中，并删除session中的code
        oAuth2SessionRelationService.addSessionAccessToken(username, clientId, code, oAuth2AccessToken.getValue());
        responseWrapper.copyBodyToResponse();
        System.out.println(1);
    }
    
}
