package daily.boot.gulimall.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<Mobile, String> {
    /**
     * 手机号校验规则
     */
    private Pattern pattern;
    
    @Override
    public void initialize(Mobile mobile) {
        pattern = Pattern.compile(mobile.regexp());
        
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }
        return pattern.matcher(value).matches();
    }
}
