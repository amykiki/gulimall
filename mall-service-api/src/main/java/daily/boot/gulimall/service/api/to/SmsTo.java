package daily.boot.gulimall.service.api.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "短信发送Vo")
@Data
public class SmsTo {
    @ApiModelProperty(value = "手机号")
    private String phone;
    
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
    
    @ApiModelProperty(value = "过期时间，单位分钟")
    private Integer expire;
}
