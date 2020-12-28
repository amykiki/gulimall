package daily.boot.gulimall.authserver.security.filters;

import daily.boot.gulimall.authserver.security.OAuth2SessionRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * /oauth/authorize 过滤器，用于把授权code和用户登录session绑定
 */
@Slf4j
public class AuthorizeCodeFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //不过滤请求，拦截授权后的响应
        filterChain.doFilter(request, response);
    
        //从响应中获取跳转url
        // location是这种格式
        // http://item.gulimall.com/user/login?code=tZnpxt&state=tv0ifI
        String location = response.getHeader("Location");
        
        if (StringUtils.isNotBlank(location) && location.contains("code=")) {
            //1. 判断redirectUrl相同
            String redirectUrl = request.getParameter(OAuth2Utils.REDIRECT_URI);
            int queryIdx = location.indexOf("?");
            //请求不包含参数，也就不包含code等
            if (queryIdx < 0) {
                return;
            }
            //请求中的redirectUrl和返回的url不相同
            String respRedirect = location.substring(0, queryIdx);
            if (!Objects.equals(redirectUrl, respRedirect)) {
                return;
            }
    
            //2. 判断state相同
            String state = request.getParameter(OAuth2Utils.STATE);
            URL url = new URL(location);
            String query = url.getQuery();
            LinkedHashMap<String, List<String>> respParams = Arrays.stream(query.split("&"))
                                                                   .map(this::splitQueryParameter)
                                                                   .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, Collectors.mapping(AbstractMap.SimpleImmutableEntry::getValue, Collectors.toList())));
    
            String respState = getParam(OAuth2Utils.STATE, respParams);

            if (!Objects.equals(state, respState)) {
                return;
            }
            
            //3. 从请求中获取session，把code属性写入session中
            String respCode = getParam("code", respParams);
            if (Objects.isNull(respCode)) {
                return;
            }
            HttpSession session = request.getSession(false);
            //以 host + code做为属性名关键字
            //正常情况下，一个host下只能有一个code
            String clientId = request.getParameter(OAuth2Utils.CLIENT_ID);
            session.setAttribute(OAuth2SessionRelationService.codeAttrName(clientId), respCode);
            //清除之前的accessToken
            session.removeAttribute(OAuth2SessionRelationService.accessTokenAttrName(clientId));
            log.info("save code-{} to session-{}", respCode, session.getId());
        }
        
    }
    
    private AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String query) {
        final int idx = query.indexOf("=");
        final String key = idx > 0 ? query.substring(0, idx) : query;
        final String value = idx > 0 && query.length() > idx + 1 ? query.substring(idx + 1) : null;
    
        try {
            AbstractMap.SimpleImmutableEntry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                    URLDecoder.decode(key, "UTF-8"),
                    URLDecoder.decode(value, "UTF-8")
            );
            return entry;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        
    }
    
    private String getParam(String paramName, LinkedHashMap<String, List<String>> paramMap) {
        List<String> results = paramMap.get(paramName);
        if (results == null || results.size() > 1) {
            return null;
        }
        return results.get(0);
    }
    
}
