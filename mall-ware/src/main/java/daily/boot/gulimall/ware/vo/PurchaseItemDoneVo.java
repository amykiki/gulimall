package daily.boot.gulimall.ware.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    @ApiModelProperty("采购需求id")
    private Long itemId;
    @ApiModelProperty("采购需求状态")
    private Integer status;
    @ApiModelProperty("采购需求失败原因")
    private String reason;
}
