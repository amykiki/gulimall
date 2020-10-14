package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券与产品关联
 * 
 * @author amy
 * @date 2020-10-14 16:05:21
 */
@Data
@TableName("sms_coupon_spu_relation")
@ApiModel(value = "优惠券与产品关联类")
public class CouponSpuRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;
    /**
     * spu_id
     */
    @ApiModelProperty(value = "spu_id")
    private Long spuId;
    /**
     * spu_name
     */
    @ApiModelProperty(value = "spu_name")
    private String spuName;

}
