package daily.boot.gulimall.authserver.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class UserLoginVo {
    private String username;
    private String password;
}
