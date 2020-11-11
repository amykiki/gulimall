package daily.boot.gulimall.search.entity;

import daily.boot.gulimall.search.elasticsearch.annotation.ESDocument;
import daily.boot.gulimall.search.elasticsearch.annotation.ESField;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ESDocument(indexName = "gulimall_products", autoExpandReplicas = true, replicas = "0-1")
@ApiModel
public class SkuEs {
    @ESId
    private Long skuId;
    
    private Long spuId;
    
    @ESField(type = ESFieldType.Text, analyzer = "ik_smart")
    private String skuTitle;
    
    @ESField(type = ESFieldType.Keyword)
    private BigDecimal skuPrice;
    
    @ESField(type = ESFieldType.Keyword)
    private String skuImg;
    
    private Long saleCount;
    
    private Boolean hasStock;
    
    private Long hotScore;
    
    private Long brandId;
    
    private Long catalogId;
    
    @ESField(type = ESFieldType.Keyword)
    private String brandName;
    
    @ESField(type = ESFieldType.Keyword)
    private String brandImg;
    
    @ESField(type = ESFieldType.Keyword)
    private String catalogName;
    
    @ESField(type = ESFieldType.Nested)
    private List<SkuAttrEs> attrs;
}
