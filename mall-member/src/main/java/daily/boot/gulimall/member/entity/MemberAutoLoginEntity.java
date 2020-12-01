package daily.boot.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import daily.boot.gulimall.common.valid.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ums_member_autologin")
@ApiModel(value = "用户自动登录标识")
public class MemberAutoLoginEntity implements Serializable {
    
    private static final long serialVersionUID = 220520613793569014L;
    @TableId(value = "series", type = IdType.INPUT)
    @ApiModelProperty("自动登录标识校验")
    @NotBlank(message = "自动登录标识series不能为空")
    private String series;
    @ApiModelProperty("用户名")
    @NotBlank(message = "自动登录标识用户名不能为空", groups = {ValidateGroup.Add.class})
    private String username;
    @ApiModelProperty("自动登录token")
    @NotBlank(message = "自动登录标识token不能为空")
    private String tokenValue;
    @ApiModelProperty("创建/修改时间")
    @NotNull(message = "自动登录标识Date不能为空", groups = {ValidateGroup.Add.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    
}
