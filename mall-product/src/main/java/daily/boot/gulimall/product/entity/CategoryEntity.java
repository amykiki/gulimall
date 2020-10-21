package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品三级分类
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_category")
@ApiModel(value = "商品三级分类类")
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId
    @ApiModelProperty(value = "分类id")
    private Long catId;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;
    /**
     * 父分类id
     */
    @ApiModelProperty(value = "父分类id")
    private Long parentCid;
    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer catLevel;
    /**
     * 是否显示[0-不显示，1显示]
     */
    @TableLogic(value = "1", delval = "0") //值与全局配置不同，所以需要配置
    @ApiModelProperty(value = "是否显示[0-不显示，1显示]")
    private Integer showStatus;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 图标地址
     */
    @ApiModelProperty(value = "图标地址")
    private String icon;
    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    private String productUnit;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer productCount;
    
    @ApiModelProperty(value = "子节点列表")
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryEntity> children;

}
