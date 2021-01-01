package daily.boot.gulimall.cart.security;

import daily.boot.gulimall.cart.to.CartUserKey;
import org.springframework.util.Assert;


public class CartUserKeyHolder {
    private static final ThreadLocal<CartUserKey> cartUserKeyHolder = new ThreadLocal<>();
    
    public static void clearCartUserKey() {
        cartUserKeyHolder.remove();
    }
    
    public static void setCartUserKey(CartUserKey cartUserKey) {
        Assert.notNull(cartUserKey, "Only non-null CartUserKey instances are permitted");
        cartUserKeyHolder.set(cartUserKey);
    }
    
    public static CartUserKey getCartUserKey() {
        return cartUserKeyHolder.get();
    }
}
