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
    private List<Long> brandId;
    @ApiModelProperty("三级分类Id")
    private Long catalog3Id;
    @ApiModelProperty("排序条件：sort=skuPrice/saleCount/hotScore_desc/asc")
    private String sort;
    @ApiModelProperty("是否显示有货")
    private Integer hasStock;
    @ApiModelProperty("价格区间查询，以_分割下限和上限,如3000_5000，3000_, _5000")
    private String skuPrice;
    @ApiModelProperty("按照属性进行筛选，属性id与属性值以_分割，多个属性值以:分割，类似5_512G:256G")
    private List<String> attrs;
    @ApiModelProperty("页码")
    private Integer pageNum = 1;
    @ApiModelProperty("每页显示个数")
    private Integer pageSize = 4;
}
