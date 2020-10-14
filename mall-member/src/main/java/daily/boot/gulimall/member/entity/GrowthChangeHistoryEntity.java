package daily.boot.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 成长值变化历史记录
 * 
 * @author amy
 * @date 2020-10-14 16:46:51
 */
@Data
@TableName("ums_growth_change_history")
@ApiModel(value = "成长值变化历史记录类")
public class GrowthChangeHistoryEntity implements Serializable {
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
     * create_time
     */
    @ApiModelProperty(value = "create_time")
    private Date createTime;
    /**
     * 改变的值（正负计数）
     */
    @ApiModelProperty(value = "改变的值（正负计数）")
    private Integer changeCount;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;
    /**
     * 积分来源[0-购物，1-管理员修改]
     */
    @ApiModelProperty(value = "积分来源[0-购物，1-管理员修改]")
    private Integer sourceType;

}
