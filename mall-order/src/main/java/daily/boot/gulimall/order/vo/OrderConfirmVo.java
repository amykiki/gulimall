package daily.boot.gulimall.order.vo;

import daily.boot.gulimall.service.api.to.MemberAddressTo;
import daily.boot.gulimall.service.api.to.OrderItemTo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderConfirmVo {
    
    /**
     * 会员收货地址列表
     */
    private List<MemberAddressTo> memberAddressTos;
    
    /**
     * 所有选中的购物项
     */
    private List<OrderItemTo> items;
    
    /**
     * 发票记录，优惠券，会员积分
     */
    private Integer integration;
    
    /**
     * 防止重复提交令牌
     */
    private String orderToken;
    
    private Map<Long, Boolean> stocks;
    
    public Integer getCount() {
        return items.stream().mapToInt(OrderItemTo::getCount).sum();
    }
    /**
     * 订单总额
     */
    public BigDecimal getTotal() {
        return items.stream()
                    .map(item -> item.getPrice().multiply(new BigDecimal(item.getCount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 应付价格
     */
    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
