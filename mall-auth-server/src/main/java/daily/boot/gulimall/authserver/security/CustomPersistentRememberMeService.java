package daily.boot.gulimall.authserver.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import daily.boot.common.exception.BusinessException;
import daily.boot.common.util.DateTimeConverterUtil;
import daily.boot.gulimall.authserver.exception.AuthErrorCode;
import daily.boot.gulimall.service.api.to.MemberAutoLoginTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@DependsOn("userAuthService")
/**
 * 参考，代码结合了这两个代码
 * https://github.com/jtalks-org/jcommune/blob/master/jcommune-view/jcommune-web-controller/src/main/java/org/jtalks/jcommune/web/rememberme/ThrottlingRememberMeService.java
 * https://articles.javatalks.ru/articles/25
 * https://github.com/jhipster/generator-jhipster/issues/3722
 */
public class CustomPersistentRememberMeService extends AbstractRememberMeServices {
    
    private final Logger log = LoggerFactory.getLogger(CustomPersistentRememberMeService.class);
    
    // Token is valid for one month
    private static final int TOKEN_VALIDITY_DAYS = 31;
    
    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;
    
    private static final int DEFAULT_SERIES_LENGTH = 16;
    
    private static final int DEFAULT_TOKEN_LENGTH = 16;
    
    private static final int UPGRADED_TOKEN_VALIDITY_SECONDS = 50;
    
    private static final String key = "123Gulimall*";
    private static final Long MAX_CACHE_SIZE = 10L;
    //需要自定义的
    /**
     * tokenRememberMeServices.setParameter(this.rememberMeParameter);
     * tokenRememberMeServices.setCookieName(this.rememberMeCookieName);
     * if (this.rememberMeCookieDomain != null) {
     * tokenRememberMeServices.setCookieDomain(this.rememberMeCookieDomain);
     * }
     * if (this.tokenValiditySeconds != null) {
     * tokenRememberMeServices.setTokenValiditySeconds(this.tokenValiditySeconds);
     * }
     * if (this.useSecureCookie != null) {
     * tokenRememberMeServices.setUseSecureCookie(this.useSecureCookie);
     * }
     * if (this.alwaysRemember != null) {
     * tokenRememberMeServices.setAlwaysRemember(this.alwaysRemember);
     * }
     */
    
    private SecureRandom random;
    
    @Resource(name = "persistentAutoLoginService")
    private PersistentAutoLoginService persistentTokenRepository;
    
    
    private Cache<String, UpgradedRememberMeToken> upgradedTokenCache = CacheBuilder.newBuilder()
                                                                                    .maximumSize(MAX_CACHE_SIZE)
                                                                                    .expireAfterWrite(UPGRADED_TOKEN_VALIDITY_SECONDS, TimeUnit.MINUTES)
                                                                                    .build();
    
    public CustomPersistentRememberMeService(@Qualifier("userAuthService") UserDetailsService userDetailsService) {
        
        super(key, userDetailsService);
        random = new SecureRandom();
    }
    
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
            HttpServletResponse response) {
        
        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];
        
        String username = null;
        
        //并发安全的获取，没问题
        UpgradedRememberMeToken upgradedToken = upgradedTokenCache.getIfPresent(presentedSeries);
        if (upgradedToken != null) {
            if (upgradedToken.isTokenCached(cookieTokens)) {
                //如果请求的cookie和上次的自动登录cookie相同，则视为短时间内并发请求，不用再去更新自动登录cookie，直接取缓存
                username = upgradedToken.getUserLoginIfValidAndRecentUpgrade(cookieTokens);
                log.debug("Detected previously upgraded login token for user '{}'", username);
                //直接改写cookie并返回，不用更新数据库，数据库里的token已经被更新了
                rewriteCookie(upgradedToken.getTo(), request, response);
            } else if (upgradedToken.isTokenCreated(cookieTokens)) {
                username = upgradedToken.getUserLoginIfValidAndRecentUpgrade(cookieTokens);
                //已经是更新过后的token，原来保存的基于seris的缓存需要删除了
                upgradedTokenCache.invalidate(presentedSeries);
                //删除后，直接返回，直接取缓存cookie，不再更新缓存，后续如果再有相同自动登录标识的请求再更新数据库内的值
                //短时间内保持cookie不变
                rewriteCookie(upgradedToken.getTo(), request, response);
            }
        }
        
        if (null == username) {
            try {
                MemberAutoLoginTo token = getPersistentToken(cookieTokens);
                username = token.getUsername();
                String newlyTokenData = generateTokenData();
                // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
                log.debug("Refreshing persistent login token for user '{}', series '{}', newlyToken '{}', old-version '{}'", username, token.getSeries(), newlyTokenData, token.getVersion());
                token.setUpdateTime(new Date());
                token.setToken(newlyTokenData);
                //可能同时有多个请求，所以更新时需要使用乐观锁
                persistentTokenRepository.updateToken(token);
                upgradedTokenCache.put(presentedSeries, new UpgradedRememberMeToken(presentedToken, token));
                rewriteCookie(token, request, response);
                
            } catch (Exception e) {
                //这里注意要判断异常类型，如果因为乐观锁更新失败，则再次从缓存中获取一次token值
                if (e instanceof BusinessException
                        //针对前一个请求已经更新了数据，后一个请求再getPersistentToken时被判断token不等于数据库的值，抛出的异常
                    && (((BusinessException) e).getCode().equals(AuthErrorCode.AUTO_TOKEN_UPDATE_ALREADY.getCode())
                        ||
                        //前一个请求已经更新了数据，后一个请求再次更新，有乐观锁的请求会失败
                        ((BusinessException) e).getCode().equals(AuthErrorCode.UPDATE_AUTO_TOKEN_FAIL.getCode()))) {
                    UpgradedRememberMeToken tokenUpdated = upgradedTokenCache.getIfPresent(presentedSeries);
                    if (tokenUpdated != null && tokenUpdated.isTokenCached(cookieTokens)) {
                        log.debug("数据库更新自动登录标识失败，从Cache中获取到最新更新后的token: 更新前series'{}', token:'{}', 更新后series'{}', token:'{}'",
                                  presentedSeries, presentedToken, tokenUpdated.getTo().getSeries(), tokenUpdated.getTo().getToken());
                        //直接改写cookie并返回，不用更新数据库，数据库里的token已经被更新了
                        if (username == null) {
                            username = tokenUpdated.getTo().getUsername();
                        }
                        rewriteCookie(tokenUpdated.getTo(), request, response);
                    } else {
                        log.error("更新自动登录标识失败，发生Businessexception: ", e);
                        throw new RememberMeAuthenticationException("更新自动登录标识失败", e);
                    }
                } else {
                    log.error("Failed to update token: ", e);
                    throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
                }
                
            }
        }
        return getUserDetailsService().loadUserByUsername(username);
    }
    
    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication
            successfulAuthentication) {
        
        String username = successfulAuthentication.getName();
        PersistentRememberMeToken token = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
        try {
            persistentTokenRepository.createNewToken(token);
            addCookie(token, request, response);
        } catch (DataAccessException e) {
            log.error("Failed to save persistent token ", e);
        }
    }
    
    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * <p>
     * The standard Spring Security implementations are too basic: they invalidate all tokens for the
     * current user, so when he logs out from one browser, all his other sessions are destroyed.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            String[] cookieTokens = decodeCookie(rememberMeCookie);
            if (cookieTokens != null && cookieTokens.length == 2) {
                persistentTokenRepository.removeUserTokenBySeries(cookieTokens[0]);
                //去除缓存token
                upgradedTokenCache.invalidate(cookieTokens[0]);
            }
        }
    }
    
    /**
     * Validate the token and return it.
     */
    private MemberAutoLoginTo getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                                             " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }
        String presentedSeries = cookieTokens[0];
        String presentedToken = cookieTokens[1];
        MemberAutoLoginTo token = persistentTokenRepository.getTokenBySeries(presentedSeries);
        
        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }
        
        // We have a match for this user/series combination
        log.info("presentedToken={} / tokenValue={}", presentedToken, token.getToken());
        if (!presentedToken.equals(token.getToken())) {
            UpgradedRememberMeToken tokenUpdated = upgradedTokenCache.getIfPresent(presentedSeries);
            if (tokenUpdated != null && tokenUpdated.isTokenCached(cookieTokens)) {
                throw new BusinessException(AuthErrorCode.AUTO_TOKEN_UPDATE_ALREADY);
            } else {
                // Token doesn't match series value. Delete this session and throw an exception.
                persistentTokenRepository.removeUserTokenBySeries(token.getSeries());
                throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous " +
                                               "cookie theft attack.");
            }
        }
        
        if (DateTimeConverterUtil.toLocalDate(token.getUpdateTime()).plusDays(TOKEN_VALIDITY_DAYS).isBefore(LocalDate.now())) {
            persistentTokenRepository.removeUserTokenBySeries(token.getSeries());
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return token;
    }
    
    private String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.getEncoder().encode(newSeries));
    }
    
    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.getEncoder().encode(newToken));
    }
    
    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        log.info("new tokenValue={}:{}", token.getSeries(), token.getTokenValue());
        setCookie(
                new String[]{token.getSeries(), token.getTokenValue()},
                TOKEN_VALIDITY_SECONDS, request, response);
    }
    
    private void rewriteCookie(MemberAutoLoginTo token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getToken()}, TOKEN_VALIDITY_SECONDS, request, response);
    }
    
    private class UpgradedRememberMeToken implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String lastToken;
        private final MemberAutoLoginTo to;
        
        public UpgradedRememberMeToken(String lastToken, MemberAutoLoginTo to) {
            super();
            this.lastToken = lastToken;
            this.to = to;
        }
        
        public boolean isTokenCached(String[] currentToken) {
            if (currentToken[0].equals(to.getSeries()) &&
                currentToken[1].equals(lastToken) &&
                (to.getUpdateTime().getTime() + UPGRADED_TOKEN_VALIDITY_SECONDS * 1000) > new Date().getTime()) {
                return true;
            }
            return false;
        }
        
        public boolean isTokenCreated(String[] currentToken) {
            if (currentToken[0].equals(to.getSeries()) &&
                currentToken[1].equals(to.getToken()) &&
                (to.getUpdateTime().getTime() + UPGRADED_TOKEN_VALIDITY_SECONDS * 1000) > new Date().getTime()) {
                return true;
            }
            return false;
        }
        
        public String getUserLoginIfValidAndRecentUpgrade(String[] currentToken) {
            if (isTokenCached(currentToken) || isTokenCreated(currentToken)) {
                return to.getUsername();
            } else {
                return null;
            }
        }
        
        public MemberAutoLoginTo getTo() {
            return to;
        }
    }
}
