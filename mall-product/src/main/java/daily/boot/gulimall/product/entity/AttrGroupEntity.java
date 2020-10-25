package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 属性分组
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_attr_group")
@ApiModel(value = "属性分组类")
public class AttrGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    @TableId
    @ApiModelProperty(value = "分组id")
    private Long attrGroupId;
    /**
     * 组名
     */
    @ApiModelProperty(value = "组名")
    private String attrGroupName;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String descript;
    /**
     * 组图标
     */
    @ApiModelProperty(value = "组图标")
    private String icon;
    /**
     * 所属分类id
     */
    @ApiModelProperty(value = "所属分类id")
    private Long catelogId;
    
    @ApiModelProperty(value = "属性对应的category的分类路径，完整的三级分类路径")
    @TableField(exist = false)
    private Long[] catelogPath;

}
