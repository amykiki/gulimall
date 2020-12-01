package daily.boot.gulimall.service.api.converter;

import daily.boot.common.util.DateTimeFormatterUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class Date2StringConverter implements Converter<Date, String> {
    @Override
    public String convert(Date source) {
        return DateTimeFormatterUtil.formatToDateTimeStr(source);
    }
}
