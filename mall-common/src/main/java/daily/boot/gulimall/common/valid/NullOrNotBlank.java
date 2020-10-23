package daily.boot.gulimall.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NullOrNotBlankConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(NullOrNotBlank.List.class)
/**
 * 用来注解字段，可以为Null，或者不为Null时，字段值不能为空字符串
 */
public @interface NullOrNotBlank {
    String message() default "{javax.validation.constraints.Size.message}";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
    
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        
        NullOrNotBlank[] value();
    }
}
