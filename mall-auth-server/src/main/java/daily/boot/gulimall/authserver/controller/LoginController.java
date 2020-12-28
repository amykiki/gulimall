package daily.boot.gulimall.authserver.controller;

import daily.boot.common.Result;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.authserver.security.OAuth2SessionRelationService;
import daily.boot.gulimall.authserver.service.FeignService;
import daily.boot.gulimall.authserver.vo.UserRegisterVo;
import daily.boot.gulimall.common.constant.AuthServerConstant;
import daily.boot.gulimall.common.exception.GuliErrorCode;
import daily.boot.gulimall.common.valid.Mobile;
import daily.boot.gulimall.service.api.to.SmsTo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.krb5.internal.AuthContext;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
@Api(tags = "用户登录注册接口")
public class LoginController {
    @Autowired
    private FeignService feignService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private OAuth2SessionRelationService relationService;
    
    @GetMapping("/login.html")
    public String loginPage(HttpSession session, Model model) {
        AuthenticationException authenticationException
                = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (Objects.nonNull(authenticationException)) {
            model.addAttribute("error", authenticationException.getMessage());
            model.addAttribute("failed", true);
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    
        return "login";
    }
    
    @ResponseBody
    @GetMapping("/sms/sendCode")
    public Result sendCode(@NotBlank(message = "手机号不能为空--controller") @Mobile @RequestParam("phone") String phone) {
        //1. 接口防刷
        String cacheCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(cacheCode)) {
            //活动存入redis时间，用当前时间减去存入redis的时间，判断用户手机号是否在60s内发送过
            long currentTime = Long.parseLong(cacheCode.split("_")[1]);
            if (System.currentTimeMillis() - currentTime < 60 * 1000) {
                //60s之内只能发送一次
                throw new BusinessException(GuliErrorCode.SMS_CODE_EXCEPTION);
            }
        }
        
        //2. 生成验证码
        String verifyCode = String.valueOf((int) (Math.random() * 900000 + 100000));
        cacheCode = verifyCode + "_" + System.currentTimeMillis();
        log.debug("注册验证码{}", verifyCode);
        //3. 保存验证码
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, cacheCode, AuthServerConstant.SMS_CODE_EXPIRE_MIN, TimeUnit.MINUTES);
        //4. 发送验证码
        SmsTo smsTo = new SmsTo();
        smsTo.setPhone(phone);
        smsTo.setVerifyCode(verifyCode);
        smsTo.setExpire(AuthServerConstant.SMS_CODE_EXPIRE_MIN);
        
        feignService.sendVerifyCode(smsTo);
        return Result.ok();
    }
    
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo registerVo, BindingResult result, RedirectAttributes attributes) {
        Map<String, String> errorMap = new HashMap<>();
        if(result.hasErrors()) {
             errorMap = result.getFieldErrors()
                              .stream()
                              .collect(Collectors.toMap(FieldError::getField,
                                                        f -> f.getDefaultMessage() != null ? f.getDefaultMessage() : "",
                                                        (v1, v2) -> v1, //在一个字段有多个错误信息时，只返回第一个错误信息
                                                        HashMap::new));
            /*
                Spring mvc return 转发跟重定向的区别
                参考
             *  https://juejin.cn/post/6844903981945192456
                return "home"
                视图解析器会自动添加前缀/page/，后缀/.jsp/。因此转发到/page/home.jsp
                url不变,还是/login
                
                
                "redirect:home"
                返回相应的url（/home）的请求。重定向到其他controller去处理
                url变为/home
                
                
                "forward:home"
                返回相应的url（/home）的请求。转发到其他controller去处理
                url不变，还是 /login
                
                参考
                探索 Spring MVC 重定向和转发
                https://isudox.com/2017/02/16/spring-mvc-redirect-forward/
                
                RedirectAttribute作用
                https://www.cnblogs.com/liuhongfeng/p/5242046.html
                1. 使用RedirectAttributes的addAttribute方法传递参数会跟随在URL后面 ，如上代码即为http:/index.action?a=a  。
                2. 使用addFlashAttribute不会跟随在URL后面，会把该参数值暂时保存于session，
                   待重定向url获取该参数后从session中移除，这里的redirect必须是方法映射路径

             */
            //校验出错，返回
            attributes.addFlashAttribute("errors", errorMap);
            attributes.addFlashAttribute("userName", registerVo.getUserName());
            attributes.addFlashAttribute("phone", registerVo.getPhone());
            //return "reg";使用返回viewname的方式，不会获取redirecAttribute的值
            return "redirect:/reg.html";
        }
        
        //1. 校验验证码
        String verifyCode = registerVo.getCode();
    
        String cacheCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone());
        if (StringUtils.isBlank(cacheCode)) {
            //验证码不存在
            errorMap.put("code", "验证码不正确");
        } else {
            if (cacheCode.contains("_") && verifyCode.equalsIgnoreCase(cacheCode.split("_")[0])) {
                //删除验证码
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone());
                
                //验证码通过，调用远程member服务注册
                Result rtn = feignService.register(registerVo);
                if (rtn.isOk()) {
                    return "redirect:/login.html";
                }else {
                    errorMap.put("msg", rtn.getMsg());
                }
            } else {
                //验证码不存在
                errorMap.put("code", "验证码不正确");
            }
        }
    
        attributes.addFlashAttribute("errors", errorMap);
        attributes.addFlashAttribute("userName", registerVo.getUserName());
        attributes.addFlashAttribute("phone", registerVo.getPhone());
        return "redirect:/reg.html";
    }
    
    /**
     * @param username 登录用户名
     * @param accessToken oauth2服务端颁发accessToken
     * 给其他内部服务调用，查看用户是否还是已登录状态
     */
    @ResponseBody
    @GetMapping("/isLogin")
    public Result<Boolean> isLogin(@NotNull(message = "用户名不能为空") String username,
                                   @NotNull(message = "请求host不能为空") String clientId,
                                   @NotNull(message = "accessToken不能为空") String accessToken) {
    
        boolean result = relationService.isUserLoginIn(username, clientId, accessToken);
        return Result.ok(result);
    }
    
}
