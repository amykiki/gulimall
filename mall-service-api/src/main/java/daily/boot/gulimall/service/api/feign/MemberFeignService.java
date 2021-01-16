package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    @GetMapping(value = "/api/member/memberreceiveaddress/{memberId}/address")
    Result<List<MemberAddressTo>> getAddress(@PathVariable("memberId") Long memberId);
    
    @GetMapping("/api/member/memberreceiveaddress/addrInfo/{addrId}")
    Result<MemberAddressTo> getByAddrId(@PathVariable("addrId") Long addrId);
    
    @GetMapping("/api/member/member/getMemberFullInfo/{userId}")
    Result<MemberFullInfoTo> getMemberFullInfo(@PathVariable("userId") Long userId);

}
