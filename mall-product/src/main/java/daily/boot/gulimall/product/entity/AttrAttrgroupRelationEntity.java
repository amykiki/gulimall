package daily.boot.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 属性&属性分组关联
 * 
 * @author amy
 * @date 2020-10-14 15:18:58
 */
@Data
@TableName("pms_attr_attrgroup_relation")
@ApiModel(value = "属性&属性分组关联类")
public class AttrAttrgroupRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 属性id
     */
    @ApiModelProperty(value = "属性id")
    private Long attrId;
    /**
     * 属性分组id
     */
    @ApiModelProperty(value = "属性分组id")
    private Long attrGroupId;
    /**
     * 属性组内排序
     */
    @ApiModelProperty(value = "属性组内排序")
    private Integer attrSort;

}
