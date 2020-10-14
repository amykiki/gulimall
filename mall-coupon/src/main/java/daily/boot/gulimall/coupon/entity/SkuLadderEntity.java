package daily.boot.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品阶梯价格
 * 
 * @author amy
 * @date 2020-10-14 16:05:20
 */
@Data
@TableName("sms_sku_ladder")
@ApiModel(value = "商品阶梯价格类")
public class SkuLadderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * spu_id
     */
    @ApiModelProperty(value = "spu_id")
    private Long skuId;
    /**
     * 满几件
     */
    @ApiModelProperty(value = "满几件")
    private Integer fullCount;
    /**
     * 打几折
     */
    @ApiModelProperty(value = "打几折")
    private BigDecimal discount;
    /**
     * 折后价
     */
    @ApiModelProperty(value = "折后价")
    private BigDecimal price;
    /**
     * 是否叠加其他优惠[0-不可叠加，1-可叠加]
     */
    @ApiModelProperty(value = "是否叠加其他优惠[0-不可叠加，1-可叠加]")
    private Integer addOther;

}
