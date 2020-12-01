package daily.boot.gulimall.authserver.security;

import daily.boot.gulimall.authserver.service.FeignService;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "userAuthService")
public class UserAuthService implements UserDetailsService {
    @Autowired
    private FeignService feignService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberUserTo memberUser = feignService.getUserInfoByUsername(username);
        if (Objects.isNull(memberUser)) {
            throw new UsernameNotFoundException("用户名" + username + "不存在");
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(memberUser, loginUser);
        if (memberUser.getStatus() != 0) {
            switch (memberUser.getStatus()) {
                case 1:
                    loginUser.setEnabled(false);
                    break;
                case 2:
                    loginUser.setAccountNonLocked(false);
                    break;
                case 3:
                    loginUser.setCredentialsNonExpired(false);
                    break;
                case 4:
                    loginUser.setAccountNonExpired(false);
                    break;
            }
        }
        return loginUser;
    }
}
