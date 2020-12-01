package daily.boot.gulimall.common.valid;

import javax.validation.groups.Default;

/**
 *  设置为继承Default的好处在于，没有声明groups的校验就已经为默认Default Group，参与所有的规则校验
 *  否则，只有明确声明相关groups的规则可以被校验
 *
 *  继承了Default.class的好处就体现在这里，只要是无论什么分组都需要校验的字段，不需要去配置分组信息。
 *  否则就需要在groups里把所有的分组写上去，非常麻烦，且不美观。
 *
 * 只要字段上配置了分组信息groups，那么只有当Controller里的分组在groups里
 * （Controller里没有分组，那么默认是Default分组）的时候，才会去校验。
 */
public interface ValidateGroup {
    interface Update extends Default {}
    interface Add extends Default {}
}
