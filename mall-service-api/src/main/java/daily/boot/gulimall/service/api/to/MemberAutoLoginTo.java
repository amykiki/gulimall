package daily.boot.gulimall.service.api.to;

import daily.boot.gulimall.common.valid.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "用户自动登录标识")
public class MemberAutoLoginTo implements Serializable {
    
    private static final long serialVersionUID = 220520613793569014L;
    @ApiModelProperty("自动登录标识校验")
    @NotBlank(message = "自动登录标识series不能为空")
    private String series;
    @ApiModelProperty("用户名")
    @NotBlank(message = "自动登录标识用户名不能为空", groups = {ValidateGroup.Add.class})
    private String username;
    @ApiModelProperty("自动登录token")
    @NotBlank(message = "自动登录标识token不能为空", groups = {ValidateGroup.Add.class})
    private String tokenValue;
    @ApiModelProperty("创建/修改时间")
    @NotNull(message = "自动登录标识Date不能为空", groups = {ValidateGroup.Add.class})
    private Date date;
    
}
