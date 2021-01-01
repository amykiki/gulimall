package daily.boot.gulimall.product.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = -8158467410939653694L;
    private Long userId;
    private String userName;
}
