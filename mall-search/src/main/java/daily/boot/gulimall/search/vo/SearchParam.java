package daily.boot.gulimall.search.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "搜索页面请求Model")
public class SearchParam {
    @ApiModelProperty("页面传递过来的全文匹配关键字")
    private String keyword;
    @ApiModelProperty("品牌Id，可以多选")
    private List<Long> brandIds;
    @ApiModelProperty("三级分类Id")
    private Long  catalogId;
    @ApiModelProperty("排序条件：sort=price/salecount/hotscore_desc/asc")
    private String sort;
    @ApiModelProperty("是否显示有货")
    private Integer hasStock;
    @ApiModelProperty("价格区间查询")
    private String skuPrice;
    @ApiModelProperty("按照属性进行筛选")
    private List<String> attr;
    @ApiModelProperty("页码")
    private Integer pageNum = 1;
}
