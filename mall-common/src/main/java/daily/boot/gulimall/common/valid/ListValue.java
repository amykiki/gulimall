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
@Constraint(validatedBy = {ListValueConstraintValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(ListValue.List.class)
public @interface ListValue {
    String message() default "{daily.boot.gulimall.common.valid.ListValue.message}";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
    
    int[] values() default {};
    
    /**
     * Defines several {@link ListValue} annotations on the same element.
     *
     * @see ListValue
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        
        ListValue[] value();
    }
    
}
