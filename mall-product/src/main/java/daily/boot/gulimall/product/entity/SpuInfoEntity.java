package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * spu信息
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_spu_info")
@ApiModel(value = "spu信息类")
public class SpuInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId
    @ApiModelProperty(value = "商品id")
    private Long id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String spuName;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String spuDescription;
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
     * 品牌名
     */
    @TableField(exist = false)
    private String brandName;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private BigDecimal weight;
    /**
     * 上架状态[0 - 新建，1 - 上架, 2 - 下架]
     */
    @ApiModelProperty(value = "上架状态[0 - 新建，1 - 上架, 2 - 下架]")
    private Integer publishStatus;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
