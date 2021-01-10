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
    
    @PostMapping("/api/member/member/register")
    Result register(@RequestBody UserRegisterTo userRegisterTo);
    
    @GetMapping("/api/member/member/userinfo-by-username")
    Result<MemberUserTo> getUserInfoByUsername(@RequestParam("username") String username);
    
    
    @PostMapping("/api/member/autologin/saveToken")
    Result createNewToken(@RequestBody MemberAutoLoginTo token);
    
    @PostMapping("/api/member/autologin/updateToken")
    Result updateToken(@RequestBody MemberAutoLoginTo updateToken);
    
    @GetMapping("/api/member/autologin/getTokenForSeries")
    Result<MemberAutoLoginTo> getTokenForSeries(@RequestParam("seriesId") String seriesId);
    
    @PostMapping("/api/member/autologin/removeUserTokens")
    Result removeUserTokens(@RequestParam("username") String username);
    
    @PostMapping("/api/member/autologin/removeUserTokenBySeries")
    Result removeUserTokenBySeries(@RequestParam("series") String series);

}
