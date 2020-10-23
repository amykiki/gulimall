package daily.boot.gulimall.common.valid;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    private Set<Integer> valueSet = new HashSet<>();
    private String listValues;
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] constraintValues = constraintAnnotation.values();
        for (int val : constraintValues) {
            valueSet.add(val);
        }
        listValues = valueSet.stream().map(String::valueOf).collect(Collectors.joining(", "));
        
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //为错误message引入额外信息变量
        context.unwrap(HibernateConstraintValidatorContext.class)
                .addExpressionVariable("listValues", listValues);
        return valueSet.contains(value);
    }
    
    
}