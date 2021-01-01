package daily.boot.gulimall.cart.security;

import daily.boot.gulimall.cart.to.CartUserKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 购物车临时用户cookie
 */
@Component
public class CartUserKeyFilter extends OncePerRequestFilter {
    @Value("${gulimall.cart.user-key-cookie-name}")
    private String userKeyCookieName;
    @Value("${gulimall.cart.user-key-cookie-max-age}")
    private int userKeyCookieMaxAge;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CartUserKey cartUserKey = new CartUserKey();
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
        if (loginUserInfo != null) {
            cartUserKey.setLoginUserInfo(loginUserInfo);
        }
    
        String userKeyCookieVal = checkUserKeyCookie(request);
        if (userKeyCookieVal != null) {
            cartUserKey.setUserKey(userKeyCookieVal);
        }
        
        if (cartUserKey.getUserKey() == null) {
            //生成前端userkey cookie
            String userKeyVal = UUID.randomUUID().toString();
            cartUserKey.setUserKey(userKeyVal);
        }
        //在response提交前设置cookie
        createUserKeyIfNeeded(response, userKeyCookieVal, cartUserKey);
        
        try {
            CartUserKeyHolder.setCartUserKey(cartUserKey);
            filterChain.doFilter(request, response);
        }finally {
            CartUserKeyHolder.clearCartUserKey();
            //注意，在这里设置cookie是不起做的，response已经提交了
            //参考解决过滤器中设置cookie无效的问题 https://my.oschina.net/huangweiindex/blog/2250261
            //createUserKeyIfNeeded(response, userKeyCookieVal, cartUserKey);
        }
    }
    
    private String checkUserKeyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie userKeyCookie = Arrays.stream(cookies)
                               .filter(cookie -> cookie.getName().equals(userKeyCookieName))
                               .findFirst().orElse(null);
        return userKeyCookie == null ? null : userKeyCookie.getValue();
    }
    
    private void createUserKeyIfNeeded(HttpServletResponse response, String userKeyCookieVal, CartUserKey cartUserKey) {
        if (userKeyCookieVal == null) {
            //参考DefaultCookieSerializer对cookie的设置
            //如果cookie中没有userKey，则设置cookie
            Cookie cookie = new Cookie(userKeyCookieName, cartUserKey.getUserKey());
            cookie.setMaxAge(userKeyCookieMaxAge);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setDomain("cart.gulimall.com");
            response.addCookie(cookie);
        }
    }
}
