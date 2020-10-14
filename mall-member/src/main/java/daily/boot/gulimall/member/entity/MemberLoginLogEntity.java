package daily.boot.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员登录记录
 * 
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@Data
@TableName("ums_member_login_log")
@ApiModel(value = "会员登录记录类")
public class MemberLoginLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * member_id
     */
    @ApiModelProperty(value = "member_id")
    private Long memberId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * ip
     */
    @ApiModelProperty(value = "ip")
    private String ip;
    /**
     * city
     */
    @ApiModelProperty(value = "city")
    private String city;
    /**
     * 登录类型[1-web，2-app]
     */
    @ApiModelProperty(value = "登录类型[1-web，2-app]")
    private Integer loginType;

}
