package daily.boot.gulimall.authserver.security;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.authserver.exception.AuthErrorCode;
import daily.boot.gulimall.service.api.feign.MemberFeignService;
import daily.boot.gulimall.service.api.to.MemberAutoLoginTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class PersistentTokenRepoImpl implements PersistentTokenRepository {
    @Autowired
    private MemberFeignService memberFeignService;
    
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        MemberAutoLoginTo to = new MemberAutoLoginTo();
        BeanUtils.copyProperties(token, to);
        try {
            Result rtn = memberFeignService.createNewToken(to);
            if (!rtn.isOk()) {
                throw new BusinessException(rtn.getMsg(), AuthErrorCode.SAVE_AUTO_TOKEN_FAIL.getCode());
            }
        } catch (Exception e) {
            log.warn("保存token{}:{}失败", token.getUsername(), to.getSeries(), e);
        }
    }
    
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        try {
            Result rtn = memberFeignService.updateToken(series, tokenValue, lastUsed);
            if (!rtn.isOk()) {
                throw new BusinessException(rtn.getMsg(), AuthErrorCode.UPDATE_AUTO_TOKEN_FAIL.getCode());
            }
        } catch (Exception e) {
            log.warn("更新token{}:{}失败", series, tokenValue, e);
        }
    }
    
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            Result<MemberAutoLoginTo> rtn = memberFeignService.getTokenForSeries(seriesId);
            if (rtn.isOk()) {
                MemberAutoLoginTo data = rtn.getData();
                return new PersistentRememberMeToken(data.getUsername(), data.getSeries(), data.getTokenValue(), data.getDate());
            }
        } catch (Exception e) {
            log.warn("getTokenForSeries失败:{}", seriesId, e);
        }
        return null;
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
