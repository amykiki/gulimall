package daily.boot.gulimall.authserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.authserver.dao.OAuthClientDetailsDao;
import daily.boot.gulimall.authserver.security.OAuthClientDetails;
import daily.boot.gulimall.authserver.service.OAuthClientDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class OAuthClientDetailsServiceImpl extends ServiceImpl<OAuthClientDetailsDao, OAuthClientDetails> implements OAuthClientDetailsService, ClientDetailsService {
    
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return this.getById(clientId);
    }
}
