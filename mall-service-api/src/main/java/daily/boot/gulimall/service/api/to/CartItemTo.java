package daily.boot.gulimall.service.api.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemTo {
    private Long skuId;
    private Boolean check = true;
    private String title;
    private String image;
    private List<String> skuAttrValues;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(this.count));
    }
}
