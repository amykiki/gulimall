package daily.boot.gulimall.service.api.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku信息
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@ApiModel(value = "sku信息类")
public class SkuInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    private Long skuId;
    /**
     * spuId
     */
    @ApiModelProperty(value = "spuId")
    private Long spuId;
    /**
     * sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    /**
     * sku介绍描述
     */
    @ApiModelProperty(value = "sku介绍描述")
    private String skuDesc;
    /**
     * 所属分类id
     */
    @ApiModelProperty(value = "所属分类id")
    private Long catelogId;
    /**
     * 品牌id
     */
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
     * 默认图片
     */
    @ApiModelProperty(value = "默认图片")
    private String skuDefaultImg;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String skuTitle;
    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String skuSubtitle;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Long saleCount;

}
