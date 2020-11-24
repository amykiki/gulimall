package daily.boot.gulimall.thirdparty.vo;

import daily.boot.gulimall.common.valid.Mobile;
import daily.boot.gulimall.common.valid.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ApiModel(value = "短信发送Vo")
@Data
/**
 * 校验参考https://www.cnkirito.moe/spring-validation/
 */
public class SmsVo {
    @ApiModelProperty(value = "手机号")
    @Mobile
    private String phone;
    
    @ApiModelProperty(value = "验证码")
    @Length(min = 4, message = "验证码长度至少为4位", groups = ValidateGroup.Add.class)
    //@Length(min = 4, message = "验证码长度至少为4位")
    @NotBlank(message = "短信验证码不能为空")
    private String verifyCode;
    @ApiModelProperty(value = "过期时间，单位分钟")
    @Positive(message = "短信验证码过期时间需要大于0")
    @NotNull(message = "短信验证码过期时间不能为空")
    private Integer expire;
}
