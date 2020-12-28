package daily.boot.gulimall.authserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;

@Slf4j
public class OAuth2SessionRelationService<S extends Session> {
    private static final String OAUTH2_CODE_ATTR = OAuth2SessionRelationService.class.getName() + ".CODE";
    private static final String OAUTH2_ACCESS_ATTR = OAuth2SessionRelationService.class.getName() + ".ACCESS";

    private FindByIndexNameSessionRepository<S> sessionRepository;
    
    public OAuth2SessionRelationService(FindByIndexNameSessionRepository<S> sessionRepository) {
        Assert.notNull(sessionRepository, "sessionRepository cannot be null");
        this.sessionRepository = sessionRepository;
    }
    
    /**
     *
     * @param username 登录用户名，前端用户
     * @param code oauth2授权码
     * @param accessToken oauth2授权token
     */
    public void addSessionAccessToken(String username, String clientId, String code, String accessToken) {
        String codeAttrName = codeAttrName(clientId);
        Collection<S> sessions = sessionRepository.findByPrincipalName(username).values();
        for (S session : sessions) {
            if (!session.isExpired() && code.equals(session.getAttribute(codeAttrName))) {
                session.removeAttribute(codeAttrName);
                session.setAttribute(accessTokenAttrName(clientId), accessToken);
                sessionRepository.save(session);
                return;
            }
        }
        log.info("Could not find Session with {}-{} to set accessToken attribute", username, code);
    }
    
    public boolean isUserLoginIn(String username, String clientId, String accessToken) {
        Collection<S> sessions = sessionRepository.findByPrincipalName(username).values();
        String tokenAttrName = accessTokenAttrName(clientId);
        for (S session : sessions) {
            if (!session.isExpired() && Objects.equals(accessToken, session.getAttribute(tokenAttrName))) {
                return true;
            }
        }
        return false;
    }
    
    public static String codeAttrName(String clientId) {
        return clientId + "_" + OAUTH2_CODE_ATTR;
    }
    
    public static String accessTokenAttrName(String clientId) {
        return clientId + "_" +  OAUTH2_ACCESS_ATTR ;
    }
}
