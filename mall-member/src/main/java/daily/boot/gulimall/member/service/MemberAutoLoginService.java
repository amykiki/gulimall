package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.member.entity.MemberAutoLoginEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Validated
public interface MemberAutoLoginService extends IService<MemberAutoLoginEntity> {
    
    void updateToken(@NotNull(message = "series不能为空") String series, @NotNull(message = "tokenValue不能为空") String tokenValue, Date lastUsed);
    
    /**
     * 删除某个用户名下的所有自动登录标识
     * @param username
     */
    void removeUserTokens(@NotNull(message = "用户名不能为空") String username);
    
}
