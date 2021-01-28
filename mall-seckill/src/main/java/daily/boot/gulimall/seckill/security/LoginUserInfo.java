package daily.boot.gulimall.seckill.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = 6224687444314954349L;
    private Long userId;
    private String userName;
}
