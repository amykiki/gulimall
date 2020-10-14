package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 品牌分类关联
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_category_brand_relation")
@ApiModel(value = "品牌分类关联类")
public class CategoryBrandRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value = "")
    private Long id;
    /**
     * 品牌id
     */
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long catelogId;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String brandName;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String catelogName;

}
