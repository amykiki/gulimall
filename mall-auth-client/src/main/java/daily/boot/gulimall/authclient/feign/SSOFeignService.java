package daily.boot.gulimall.authclient.feign;

import daily.boot.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gulimall-auth-server")
public interface SSOFeignService {
    
    @GetMapping("/isLogin")
    Result<Boolean> isLogin(@RequestParam("username") String username,
                            @RequestParam("clientId") String clientId,
                            @RequestParam("accessToken") String accessToken);



}
