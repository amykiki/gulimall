package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.common.valid.ValidateGroup;
import daily.boot.gulimall.service.api.to.MemberAutoLoginTo;
import daily.boot.gulimall.service.api.to.MemberUserTo;
import daily.boot.gulimall.service.api.to.UserRegisterTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

@FeignClient(name = "${gulimall.feign.member}")
public interface MemberFeignService {
    
    @PostMapping("/member/member/register")
    Result register(@RequestBody UserRegisterTo userRegisterTo);
    
    @GetMapping("/member/member/userinfo-by-username")
    Result<MemberUserTo> getUserInfoByUsername(@RequestParam("username") String username);
    
    
    @PostMapping("/member/autologin/saveToken")
    Result createNewToken(@RequestBody MemberAutoLoginTo token);
    
    @PostMapping("/member/autologin/updateToken")
    Result updateToken(@RequestParam("series") String series, @RequestParam("tokenValue") String tokenValue, @RequestParam("lastUsed") Date lastUsed);
    
    @GetMapping("/member/autologin/getTokenForSeries")
    Result<MemberAutoLoginTo> getTokenForSeries(@RequestParam("seriesId") String seriesId);
    
    @PostMapping("/member/autologin/removeUserTokens")
    Result removeUserTokens(@RequestParam("username") String username);
    
    @PostMapping("/member/autologin/removeUserTokenBySeries")
    Result removeUserTokenBySeries(@RequestParam("series") String series);

}
