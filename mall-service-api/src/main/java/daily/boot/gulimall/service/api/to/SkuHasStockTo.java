package daily.boot.gulimall.service.api.to;

import lombok.Data;

@Data
public class SkuHasStockTo {
    private Long skuId;
    private Boolean hasStock;
}
