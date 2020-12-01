package daily.boot.gulimall.service.api.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户TO")
@Data
public class MemberUserTo {
    private Long id;
    /**
     * 会员等级id
     */
    @ApiModelProperty(value = "会员等级id")
    private Long levelId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "启用状态, 0 = inuse, 1 = disable, 2 = lock, 3 = password expire, 4 = account expire")
    private Integer status;
}
