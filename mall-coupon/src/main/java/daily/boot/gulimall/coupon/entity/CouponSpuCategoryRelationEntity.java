package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券分类关联
 * 
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@Data
@TableName("sms_coupon_spu_category_relation")
@ApiModel(value = "优惠券分类关联类")
public class CouponSpuCategoryRelationEntity implements Serializable {
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
     * 产品分类id
     */
    @ApiModelProperty(value = "产品分类id")
    private Long categoryId;
    /**
     * 产品分类名称
     */
    @ApiModelProperty(value = "产品分类名称")
    private String categoryName;

}
