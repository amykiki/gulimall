package daily.boot.gulimall.service.api.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemEntityTo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * order_id
     */
    @ApiModelProperty(value = "order_id")
    private Long orderId;
    /**
     * order_sn
     */
    @ApiModelProperty(value = "order_sn")
    private String orderSn;
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
    /**
     * spu_pic
     */
    @ApiModelProperty(value = "spu_pic")
    private String spuPic;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String spuBrand;
    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
    private Long categoryId;
    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    private Long skuId;
    /**
     * 商品sku名字
     */
    @ApiModelProperty(value = "商品sku名字")
    private String skuName;
    /**
     * 商品sku图片
     */
    @ApiModelProperty(value = "商品sku图片")
    private String skuPic;
    /**
     * 商品sku价格
     */
    @ApiModelProperty(value = "商品sku价格")
    private BigDecimal skuPrice;
    /**
     * 商品购买的数量
     */
    @ApiModelProperty(value = "商品购买的数量")
    private Integer skuQuantity;
    /**
     * 商品销售属性组合（JSON）
     */
    @ApiModelProperty(value = "商品销售属性组合（JSON）")
    private String skuAttrsVals;
    /**
     * 商品促销分解金额
     */
    @ApiModelProperty(value = "商品促销分解金额")
    private BigDecimal promotionAmount;
    /**
     * 优惠券优惠分解金额
     */
    @ApiModelProperty(value = "优惠券优惠分解金额")
    private BigDecimal couponAmount;
    /**
     * 积分优惠分解金额
     */
    @ApiModelProperty(value = "积分优惠分解金额")
    private BigDecimal integrationAmount;
    /**
     * 该商品经过优惠后的分解金额
     */
    @ApiModelProperty(value = "该商品经过优惠后的分解金额")
    private BigDecimal realAmount;
    /**
     * 赠送积分
     */
    @ApiModelProperty(value = "赠送积分")
    private Integer giftIntegration;
    /**
     * 赠送成长值
     */
    @ApiModelProperty(value = "赠送成长值")
    private Integer giftGrowth;
}
