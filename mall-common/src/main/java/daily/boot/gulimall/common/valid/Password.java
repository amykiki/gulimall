package daily.boot.gulimall.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

/**
 * 复合校验索引，参考
 * https://stackoverflow.com/questions/16225015/multiple-regex-patterns-for-1-field
 * 关于正则匹配[]里无需转移的字符
 * https://stackoverflow.com/questions/19976018/does-a-dot-have-to-be-escaped-in-a-character-class-square-brackets-of-a-regula
 * 正则匹配无需转义字符
 * http://www.regular-expressions.info/refcharclass.html
 * 复合校验注解参考
 * https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html_single/#section-boolean-constraint-composition
 * https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html_single/#validator-customconstraints-compound
 * 复合校验注解文档
 * https://beanvalidation.org/1.1/spec/#constraintsdefinitionimplementation-constraintcomposition
 */
@NotNull(message = "密码不能为空")
@Size(min = 6, max = 18, message = "密码必须为6-18位字符")
@Pattern.List({
        @Pattern(regexp = "(?=.*[0-9]).+", message = "密码必须至少包含一个数字"),
        @Pattern(regexp = "(?=.*[a-z]).+", message = "密码必须至少包含一个小写字符"),
        @Pattern(regexp = "(?=.*[A-Z]).+", message = "密码必须至少包含一个大写字符"),
        @Pattern(regexp = "(?=.*[!@#$%^&*+=?\\-_()/\".,<>~`;:{}\\[\\]\\\\]).+", message = "密码必须至少包含一个特殊字符"),
        @Pattern(regexp = "(?=^\\S+$).+", message = "密码不能包含空白字符")
})
/**
 * @ReportAsSingleViolation作用，一旦有注解失败则停止
 * @ReportAsSingleViolation that only one single violations will be generated and validation will stop after first failure in this case
 * https://beanvalidation.org/1.1/spec/#constraintsdefinitionimplementation-constraintcomposition
 * 但是ReportAsSingleViolation的坏处是，会忽略到真实的错误信息，而只返回定义的总的default message，如本类的密码格式不正确
 * 参考
 * https://stackoverflow.com/questions/3925736/different-hibernate-validation-annotations-on-same-property
 */
//@ReportAsSingleViolation
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface Password {
    String message() default "密码格式不正确";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
}
