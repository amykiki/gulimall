package daily.boot.gulimall.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AttrRespVo extends AttrVo {
    @ApiModelProperty(value = "商品品类名")
    private String catelogName;
    @ApiModelProperty(value = "属性分组名")
    private String groupName;
    @ApiModelProperty(value = "商品品类三级路径")
    private Long[] catelogPath;
}
