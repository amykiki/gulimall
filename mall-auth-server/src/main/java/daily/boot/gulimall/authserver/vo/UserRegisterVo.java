package daily.boot.gulimall.authserver.vo;

import daily.boot.gulimall.common.valid.Mobile;
import daily.boot.gulimall.common.valid.Password;
import daily.boot.gulimall.common.valid.Username;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString(exclude = "password")
@ApiModel("用户注册VO")
public class UserRegisterVo {
    @Username
    private String userName;
    
    @Password
    private String password;
    
    @NotNull(message = "手机号不能为空")
    @Mobile
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
}
