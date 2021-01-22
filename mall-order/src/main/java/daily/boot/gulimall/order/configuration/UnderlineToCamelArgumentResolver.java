package daily.boot.gulimall.order.configuration;

import daily.boot.common.exception.BusinessException;
import daily.boot.common.exception.error.CommonErrorCode;
import daily.boot.gulimall.order.vo.AliPayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
/**
 * 参考 spring-boot 下划线和驼峰转换 https://adolphor.com/2019/11/16/spring-boot-under-lower-camel.html
 * 之所以需要这个转化，是因为阿里回调的异步通知的参数格式是下划线的，需要转为驼峰
 * 参考 AliPayAsyncVo对象
 * 注册参考
 */
public class UnderlineToCamelArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 匹配下划线的格式
     */
    private static final Pattern pattern = Pattern.compile("_(\\w)");
    
    private String underLineToCamel(String paramName) {
        Matcher matcher = pattern.matcher(paramName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AliPayAsyncVo.class);
        // 全局使用的话，直接返回true
        //return true;
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object obj;
        try {
            obj = parameter.getParameterType().newInstance();
        } catch (Exception e) {
            log.error("无法实例化的数据类型: {}", parameter.getParameterType());
            throw new BusinessException(CommonErrorCode.PARAM_ERROR);
        }
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        //date类型转换
        beanWrapper.registerCustomEditor(Date.class, new CustomDateEditor());
        Iterator<String> parameterNames = webRequest.getParameterNames();
        while (parameterNames.hasNext()) {
            String paramName = parameterNames.next();
            String val = webRequest.getParameter(paramName);
            try {
                //这里注册类型转换器没用
                beanWrapper.setPropertyValue(underLineToCamel(paramName), val);
            } catch (BeansException e) {
                log.info("下划线转驼峰时出错，实体类 {} 中无对应属性: {}", obj.getClass().getName(), paramName);
            }
        }
        return obj;
    }
}
