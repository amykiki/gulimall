package daily.boot.gulimall.cart.to;

import daily.boot.gulimall.cart.security.LoginUserInfo;
import lombok.Data;

@Data
public class CartUserKey {
    private LoginUserInfo loginUserInfo;
    private String userKey;
}
