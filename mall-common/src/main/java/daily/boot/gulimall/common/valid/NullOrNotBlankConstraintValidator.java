package daily.boot.gulimall.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串属性可以为Null，当不为Null时，不能为空
 * 参考https://stackoverflow.com/questions/31132477/java-annotation-for-null-but-neither-empty-nor-blank
 */
public class NullOrNotBlankConstraintValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.trim().length() > 0;
    }
}
