package daily.boot.gulimall.cart.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = 8721729969250734831L;
    private Long userId;
    private String userName;
}
