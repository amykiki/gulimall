package daily.boot.gulimall.cart.service;

import daily.boot.gulimall.cart.vo.CartItemVo;
import daily.boot.gulimall.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {
    List<CartItemVo> getUserCartItems(Long loginUserId);
    
    CartVo getCart() throws ExecutionException, InterruptedException;
    
    void addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
    
    CartItemVo getCartItem(Long skuId);
    
    void checkItem(Long skuId, Integer checked);
    
    void changeItemCount(Long skuId, Integer num);
    
    void deleteCartInfo(Long skuId);
}
