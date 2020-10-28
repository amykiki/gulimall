package daily.boot.gulimall.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class AttrGroupWithAttrsVo implements Serializable {
    
    private static final long serialVersionUID = -782148128710628086L;
    
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
    
    List<AttrVo> attrs;
}
