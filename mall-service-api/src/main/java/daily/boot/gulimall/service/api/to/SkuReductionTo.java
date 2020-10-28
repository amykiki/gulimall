package daily.boot.gulimall.service.api.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuReductionTo {
    private Long skuId;
    //满减折扣数量
    private int fullCount;
    //满减折扣
    private BigDecimal discount;
    //是否叠加优惠
    private int countStatus;
    //满减金额
    private BigDecimal fullPrice;
    //满减折扣金额
    private BigDecimal reducePrice;
    //是否叠加优惠
    private int priceStatus;
    //会员价
    private List<MemberPriceTo> memberPrice;
    
}
