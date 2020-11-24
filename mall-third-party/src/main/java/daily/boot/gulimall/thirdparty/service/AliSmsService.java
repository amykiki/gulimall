package daily.boot.gulimall.thirdparty.service;

import daily.boot.gulimall.common.valid.ValidateGroup;
import daily.boot.gulimall.thirdparty.vo.SmsVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * 短信发送服务
 */
@Validated(Default.class)
public interface AliSmsService {
    //service层方法分组，@Validated必须写在方法上，写在参数上不不起作用
    //bean校验，只能用@Valid，用@Validated不起作用，这个和Controller不一样
    //参考
    // https://juejin.cn/post/6844903800877105159#heading-8
    //https://my.oschina.net/fangshixiang/blog/3079819
    @Validated(ValidateGroup.Add.class)
    boolean sendRegVerifyCode(@NotNull @Valid SmsVo smsVo);
}
