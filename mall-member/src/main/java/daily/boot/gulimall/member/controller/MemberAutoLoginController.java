package daily.boot.gulimall.member.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.common.valid.ValidateGroup;
import daily.boot.gulimall.member.entity.MemberAutoLoginEntity;
import daily.boot.gulimall.member.service.MemberAutoLoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/member/autologin")
@Api(tags = "用户自动登录管理接口")
public class MemberAutoLoginController {
    @Autowired
    private MemberAutoLoginService autoLoginService;
    
    @PostMapping("/saveToken")
    public Result createNewToken(@RequestBody @Validated({ValidateGroup.Add.class}) MemberAutoLoginEntity token) {
        autoLoginService.save(token);
        return Result.ok();
    }
    @PostMapping("/updateToken")
    public Result updateToken(@RequestBody @Validated({ValidateGroup.Update.class}) MemberAutoLoginEntity updateToken){
        autoLoginService.updateToken(updateToken);
        return Result.ok();
    }
    
    @GetMapping("/getTokenForSeries")
    public Result<MemberAutoLoginEntity> getTokenForSeries(String seriesId){
        MemberAutoLoginEntity token = autoLoginService.getById(seriesId);
        return Result.ok(token);
    }
    @PostMapping("/removeUserTokens")
    public Result removeUserTokens(String username){
        autoLoginService.removeUserTokens(username);
        return Result.ok();
    }
    
    @PostMapping("/removeUserTokenBySeries")
    public Result removeUserTokenBySeries(String series){
        autoLoginService.removeById(series);
        return Result.ok();
    }
}
