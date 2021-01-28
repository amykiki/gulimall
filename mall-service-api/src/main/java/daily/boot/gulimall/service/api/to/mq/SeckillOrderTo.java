package daily.boot.gulimall.service.api.to.mq;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

@Builder
@Data
public class SeckillOrderTo {
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    
    /**
     * 购买数量
     */
    private Integer num;
    
    /**
     * 会员ID
     */
    private Long memberId;
    
    @Tolerate
    public SeckillOrderTo() {
    }
}
