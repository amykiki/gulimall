package daily.boot.gulimall.search.vo;

import daily.boot.gulimall.search.entity.SkuEs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("搜索页面结果Model")
public class SkuSearchResult {
    @ApiModelProperty("查询到的所有商品信息")
    private List<SkuEs> product;
    @ApiModelProperty("当前页码")
    private Integer pageNum;
    @ApiModelProperty("总记录数")
    private Long total;
    @ApiModelProperty("总页码数")
    private Integer totalPages;
    @ApiModelProperty("当前查询到的结果，所涉及到的品牌")
    private List<BrandVo> brands;
    @ApiModelProperty("当前查询到的结果，所有涉及到的属性")
    private List<AttrVo> attrs;
    @ApiModelProperty("当前查询到的结果，所有涉及到的分类")
    private List<CatalogVo> catalogs;
    @ApiModelProperty("面包屑导航数据")
    private List<NavVo> navs;
    
    @Data
    @ApiModel("返回给前端品牌信息")
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    
    @Data
    @ApiModel("返回给前端属性信息")
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
    
    @Data
    @ApiModel("返回给前端分类信息")
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }
    
    @Data
    @ApiModel("返回给前端面包屑信息")
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }
}
