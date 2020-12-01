package daily.boot.gulimall.service.api.converter;

import daily.boot.common.util.DateTimeFormatterUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class String2DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        return DateTimeFormatterUtil.parseToDefaultDate(source);
    }
}
