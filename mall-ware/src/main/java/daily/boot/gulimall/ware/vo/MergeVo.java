package daily.boot.gulimall.ware.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MergeVo {
    @ApiModelProperty("采购单ID")
    private Long purchaseId; //采购单ID
    @ApiModelProperty("待合并采购项ID")
    private List<Long> items; //待合并采购项ID
}
