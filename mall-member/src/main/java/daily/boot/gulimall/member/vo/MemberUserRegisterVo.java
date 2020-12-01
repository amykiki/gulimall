package daily.boot.gulimall.member.vo;

import daily.boot.gulimall.common.valid.Mobile;
import daily.boot.gulimall.common.valid.Password;
import daily.boot.gulimall.common.valid.Username;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Member服务用户注册Vo")
public class MemberUserRegisterVo {
    
    @Username
    private String userName;
    
    @Password
    private String password;
    
    @Mobile
    @NotNull(message = "手机号不能为空")
    private String phone;
}
