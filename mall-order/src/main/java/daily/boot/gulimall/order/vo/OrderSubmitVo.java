package daily.boot.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubmitVo {
    /**
     * 收货地址id
     */
    private Long addrId;
    
    /**
     * 支付方式
     */
    private Integer payType;
    
    //无需提交要购买的商品，去购物车再获取一遍
    
    // TODO: 2021/1/11 优惠发票
    
    /**
     * 防重令牌
     */
    private String orderToken;
    
    /**
     * 应付价格
     */
    private BigDecimal payPrice;
    
    /**
     * 订单备注
     */
    private String remarks;
}
