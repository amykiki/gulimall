package daily.boot.gulimall.authserver.security;

import com.fasterxml.jackson.annotation.*;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails, Serializable {
    private static final long serialVersionUID = -787225691577801742L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonIgnore
    private boolean accountNonExpired = true;
    @JsonIgnore
    private boolean accountNonLocked = true;
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    @JsonIgnore
    private boolean enabled = true;
    @Override
    @JsonProperty("authorities")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    
    public Long getId() {
        return id;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 配置了 .sessionManagement()
     *       .maximumSessions(1);
     * 登录并发控制情况下，需要登录成功后会调用RegisterSessionAuthenticationStrategy注册当前session
     * 而实际注册SessionRegistry#registerNewSession注册
     * 这里会给每个principal创建有效的session Set
     * 定义如下private final ConcurrentMap<Object, Set<String>> principals;
     * key是principal。
     * 所以如果不改下equals和hashCode方法，LoginUser是一个可变对象，每次生成的对象hashCode都会变，导致maximumSessions配置永不会生效
     *
     * 所以必须改写
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUser)) return false;
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(username, loginUser.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
