package daily.boot.gulimall.authserver.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import daily.boot.gulimall.service.api.to.MemberAutoLoginTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 参考https://github.com/jhipster/generator-jhipster/issues/3722
 * sprng-security内置的自动登录服务无法处理并发请求的问题
 */
@Slf4j
public class CustomPersistentRememberMeTmpServices extends AbstractRememberMeServices {
    private static final int UPGRADED_TOKEN_VALIDITY_SECONDS = 5;
    private Cache<String, MemberAutoLoginTo> upgradedTokenCache = CacheBuilder.newBuilder()
                                                                              .expireAfterWrite(UPGRADED_TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS)
                                                                              .build();
    
    private PersistentTokenRepository persistentAutoLoginService;
    
    protected CustomPersistentRememberMeTmpServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        super(key, userDetailsService);
        this.persistentAutoLoginService = persistentTokenRepository;
    }
    
    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
    }
    
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2
                                             + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }
        
        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];
    
        MemberAutoLoginTo upgradedAutoLogin = upgradedTokenCache.getIfPresent(presentedSeries);
        if (upgradedAutoLogin != null) {
        
        } else {
            //还没有放进缓存，注意，此时可能有多个线程竞争，需要判断数据库的值
            
        }
        return null;
    }
    
    private boolean isValidCookieToken(String cookieSeries, String cookieToken, MemberAutoLoginTo loginTo) {
        if (loginTo.getSeries().equals(cookieSeries)
            && loginTo.getVersion().equals(cookieToken)
            && (loginTo.getUpdateTime().getTime() + UPGRADED_TOKEN_VALIDITY_SECONDS * 1000) > new Date().getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
