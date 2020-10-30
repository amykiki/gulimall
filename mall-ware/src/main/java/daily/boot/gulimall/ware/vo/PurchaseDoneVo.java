package daily.boot.gulimall.ware.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PurchaseDoneVo {
    @ApiModelProperty(value = "采购单ID")
    private Long id;
    @ApiModelProperty(value = "采购需求完成详情")
    private List<PurchaseItemDoneVo> items;
}
