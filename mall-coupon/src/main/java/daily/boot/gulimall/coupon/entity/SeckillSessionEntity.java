package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 秒杀活动场次
 * 
 * @author amy
 * @date 2020-10-14 16:05:21
 */
@Data
@TableName("sms_seckill_session")
@ApiModel(value = "秒杀活动场次类")
public class SeckillSessionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 场次名称
     */
    @ApiModelProperty(value = "场次名称")
    private String name;
    /**
     * 每日开始时间
     */
    @ApiModelProperty(value = "每日开始时间")
    private Date startTime;
    /**
     * 每日结束时间
     */
    @ApiModelProperty(value = "每日结束时间")
    private Date endTime;
    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @TableField(exist = false)
    private List<SeckillSkuRelationEntity> relationSkus;

}
