package daily.boot.gulimall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo {
    /**
     * 购物车子项信息
     */
    private List<CartItemVo> items;
    /**
     * 购物车总商品数量
     */
    private Integer countNum;
    /**
     * 购物车总商品类型数量
     */
    private Integer countType;
    /**
     * 购物车总商品价格
     */
    private BigDecimal totalAmount;
    /**
     * 减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");
    
    public Integer getCountNum() {
        return items.stream().mapToInt(CartItemVo::getCount).sum();
    }
    
    public Integer getCountType() {
        return items.stream().mapToInt(i -> 1).sum();
    }
    
    public BigDecimal getTotalAmount() {
        BigDecimal amount = items.stream()
                                 .map(CartItemVo::getTotalPrice)
                                 .reduce(BigDecimal.ZERO, BigDecimal::add);
        //计算优惠后的价格
        return amount.subtract(getReduce());
    }
}
