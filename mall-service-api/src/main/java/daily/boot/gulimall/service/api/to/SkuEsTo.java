package daily.boot.gulimall.service.api.to;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "SKU-ES-TO-Model")
public class SkuEsTo {
    private Long skuId;
    
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attr> attrs;
    
    @Data
    @ApiModel
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attr {
        private Long attrId;
        private String attrName;
        private String attrValue;
        
    }
}
