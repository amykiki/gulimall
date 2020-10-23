package daily.boot.gulimall.common.valid;

import javax.validation.groups.Default;

/**
 *  设置为继承Default的好处在于，没有声明groups的校验就已经为默认Default Group，参与所有的规则校验
 *  否则，只有明确声明相关groups的规则可以被校验
 */
public interface ValidateGroup {
    interface Update extends Default {}
    interface Add extends Default {}
}
