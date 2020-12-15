package daily.boot.gulimall.authserver.security;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.*;
import daily.boot.gulimall.authserver.typehandlers.ListAuthorityTypeHandler;
import daily.boot.gulimall.authserver.typehandlers.MapStringTypeHandler;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@TableName(value = "oauth_client_details", autoResultMap = true)
@ApiModel("自动登录客户端信息")
/**
 * 注意，mybatis-plus使用typeHandler有坑，插入的时候有效，查询的时候无效
 * 具体看这个链接 【mybatis plus TableField typehandler 问题】https://github.com/baomidou/mybatis-plus/issues/357
 * 和
 * 【mybatis plus坑之 - @TableField(typeHandler) 查询时不生效为null】 https://blog.csdn.net/sgambler/article/details/106921634
 * 加了autoResultMap = true的确解决了问题
 */
public class OAuthClientDetails implements ClientDetails {
    @TableId
    private String clientId;
    private String clientSecret;
    //@JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
    @TableField()
    private Set<String> scope = Collections.emptySet();
    //@JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
    private Set<String> resourceIds = Collections.emptySet();
    //@JsonDeserialize(using = Jackson2ArrayOrStringDeserializer.class)
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    private Set<String> registeredRedirectUri;
    private Set<String> autoApproveScopes;
    @TableField(typeHandler = ListAuthorityTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<GrantedAuthority> authorities = Collections.emptyList();
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    @JsonIgnore
    @TableField(typeHandler = MapStringTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
    
    
    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        
        for (String auto : autoApproveScopes) {
            if (auto.equals("true") || scope.equals(auto)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public void setAutoApproveScopes(Collection<String> autoApproveScopes) {
        this.autoApproveScopes = new HashSet<>(autoApproveScopes);
    }
    
    public Set<String> getAutoApproveScopes() {
        return autoApproveScopes;
    }
    
    @Override
    @JsonIgnore
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }
    
    @Override
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    @Override
    @JsonIgnore
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }
    
    @Override
    public Set<String> getScope() {
        return scope;
    }
    
    public void setScope(Collection<String> scope) {
        this.scope = scope == null ? Collections.emptySet()
                                   : new LinkedHashSet<>(scope);
    }
    
    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }
    
    public void setResourceIds(Collection<String> resourceIds) {
        this.resourceIds = resourceIds == null ? Collections.<String>emptySet() : new LinkedHashSet<>(resourceIds);
    }
    
    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return this.authorizedGrantTypes;
    }
    
    public void setAuthorizedGrantTypes(Collection<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = new LinkedHashSet<>(authorizedGrantTypes);
    }
    
    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }
    
    public void setRegisteredRedirectUri(Collection<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri == null ? null : new LinkedHashSet<>(registeredRedirectUri);
    }
    
    @JsonProperty("authorities")
    private List<String> getAuthoritiesAsStrings() {
        return new ArrayList<>(AuthorityUtils.authorityListToSet(authorities));
    }
    
    @JsonProperty("authorities")
    private void setAuthoritiesAsStrings(Set<String> values) {
        setAuthorities(AuthorityUtils.createAuthorityList(values.toArray(new String[0])));
    }
    
    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @JsonIgnore
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = new ArrayList<>(authorities);
    }
    
    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }
    
    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }
    
    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }
    
    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }
    
    @Override
    @JsonAnyGetter
    public Map<String, Object> getAdditionalInformation() {
        return Collections.unmodifiableMap(this.additionalInformation);
    }
    
    public void setAdditionalInformation(Map<String, ?> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<>(additionalInformation);
    }
    
    @JsonAnySetter
    //map的串行序列化
    public void addAdditionalInformation(String key, Object value) {
        this.additionalInformation.put(key, value);
    }
    
    
    @Override
    public String toString() {
        return "BaseClientDetails [clientId=" + clientId + ", clientSecret="
               + clientSecret + ", scope=" + scope + ", resourceIds="
               + resourceIds + ", authorizedGrantTypes="
               + authorizedGrantTypes + ", registeredRedirectUris="
               + registeredRedirectUri + ", authorities=" + authorities
               + ", accessTokenValiditySeconds=" + accessTokenValiditySeconds
               + ", refreshTokenValiditySeconds="
               + refreshTokenValiditySeconds + ", additionalInformation="
               + additionalInformation + "]";
    }
    
}
