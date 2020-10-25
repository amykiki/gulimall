package daily.boot.gulimall.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "商品BrandVO")
public class BrandVo implements Serializable {
    private static final long serialVersionUID = 4731416398557455039L;
    
    @ApiModelProperty(value = "商品ID")
    private Long brandId;
    @ApiModelProperty(value = "商品名")
    private String name;
}
