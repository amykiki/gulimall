package daily.boot.gulimall.product.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "分组属性和规格参数关联VO")
public class AttrGroupRelationVo implements Serializable {
    private static final long serialVersionUID = 4800374318934811746L;
    
    private Long attrId;
    private Long attrGroupId;
}
