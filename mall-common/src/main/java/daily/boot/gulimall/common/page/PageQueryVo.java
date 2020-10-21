package daily.boot.gulimall.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("前端分页查询页面参数")
public class PageQueryVo {
    @ApiModelProperty(value = "当前页", required = true)
    private Long page;
    @ApiModelProperty(value = "每页显示数目", required = true)
    private Long limit;
    @ApiModelProperty(value = "排序字段")
    private String orderFiled;
    @ApiModelProperty(value = "排序方式", allowableValues = "asc, desc")
    private String order;
    @ApiModelProperty(value = "通用查询参数")
    private String key;
}
