package daily.boot.gulimall.authserver.security;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.authserver.exception.AuthErrorCode;
import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.to.MemberAutoLoginTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("persistentAutoLoginService")
@Slf4j
public class PersistentAutoLoginService implements PersistentTokenRepository {
    @Autowired
    private MemberFeignService memberFeignService;
    
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        MemberAutoLoginTo to = new MemberAutoLoginTo();
        to.setSeries(token.getSeries());
        to.setUsername(token.getUsername());
        to.setToken(token.getTokenValue());
        to.setUpdateTime(token.getDate());
        createNewToken(to);
    }
    
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        MemberAutoLoginTo to = new MemberAutoLoginTo();
        to.setSeries(series);
        to.setToken(tokenValue);
        to.setUpdateTime(lastUsed);
        updateToken(to);
    }
    
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        MemberAutoLoginTo to = getTokenBySeries(seriesId);
        if (to != null) {
            return new PersistentRememberMeToken(to.getUsername(), to.getSeries(), to.getToken(), to.getUpdateTime());
        }
        return null;
    }
    
    
    public void createNewToken(MemberAutoLoginTo to) {
        try {
            to.setVersion(0);
            Result rtn = memberFeignService.createNewToken(to);
            if (!rtn.isOk()) {
                throw new BusinessException(rtn.getMsg(), AuthErrorCode.SAVE_AUTO_TOKEN_FAIL.getCode());
            }
        } catch (Exception e) {
            log.warn("保存token{}:{}失败", to.getUsername(), to.getSeries(), e);
        }
    }
    
    public MemberAutoLoginTo getTokenBySeries(String seriesId) {
        try {
            Result<MemberAutoLoginTo> rtn = memberFeignService.getTokenForSeries(seriesId);
            if (rtn.isOk()) {
                return rtn.getData();
            }
        } catch (Exception e) {
            log.warn("getTokenForSeries失败:{}", seriesId, e);
        }
        return null;
    }
    
    public void updateToken(MemberAutoLoginTo to) {
        try {
            Result rtn = memberFeignService.updateToken(to);
            if (!rtn.isOk()) {
                if (rtn.getCode().equals("MEM-004")) {
                    throw new BusinessException(rtn.getMsg(), AuthErrorCode.UPDATE_AUTO_TOKEN_FAIL.getCode());
                } else {
                    throw new RememberMeAuthenticationException("自动登录标识失败，错误码:" + rtn.getCode() + "错误信息:" + rtn.getMsg());
                }
            }
        } catch (Exception e) {
            log.warn("更新token{}:token-{}:version-{}失败", to.getSeries(), to.getToken(), to.getVersion());
            throw e;
        }
    }
    
    
    
    @Override
    public void removeUserTokens(String username) {
        try {
            memberFeignService.removeUserTokens(username);
        } catch (Exception e) {
            log.warn("删除{}下的所有自动登录凭证失败", username, e);
        }
    
    }
    
    public void removeUserTokenBySeries(String series) {
        try {
            memberFeignService.removeUserTokenBySeries(series);
        } catch (Exception e) {
            log.warn("删除Series{}下的自动登录凭证失败", series, e);
        }
    }
}
