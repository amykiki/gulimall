package daily.boot.gulimall.service.api.to;

import lombok.Data;

@Data
public class UserRegisterTo {
    private String userName;
    private String password;
    private String phone;
}
