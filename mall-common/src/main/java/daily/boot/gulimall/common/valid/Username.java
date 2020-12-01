package daily.boot.gulimall.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 用户名匹配规则，参考
 * https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
 * 1. 用户名长度6-20个字符
 * 2. 用户名只能包含字母，数字，中文字符，下划线
 * 3. 用户名不能以下划线开头和结尾
 * 4. 用户名不能包含连续下划线
 * 5. 用户名不能纯数字
 * 参考
 * ^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$
 *  └─────┬────┘└───┬──┘└─────┬─────┘└─────┬─────┘ └───┬───┘
 *        │         │         │            │           no _ or . at the end
 *        │         │         │            │
 *        │         │         │            allowed characters
 *        │         │         │
 *        │         │         no __ or _. or ._ or .. inside
 *        │         │
 *        │         no _ or . at the beginning
 *        │
 *        username is 8-20 characters long
 */
@NotNull(message = "用户名不能为空")
@Pattern(regexp = "^(?=.{6,20}$)(?!\\d+$)(?!_)(?!.*__)[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]+(?<!_)$",
        message = "用户名6-20字符，包含字母/数字/下划线,不能以下划线/数字开头")
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface Username {
    String message() default "用户名格式不正确，用户名6-20字符，包含字母/数字/下划线,不能以下划线/数字开头和下划线结尾，不能包含连续的下划线.";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
}
