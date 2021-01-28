package daily.boot.gulimall.service.api.formatter;

import daily.boot.common.util.DateTimeFormatterUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {
    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return DateTimeFormatterUtil.parseToDefaultDate(text);
    }
    
    @Override
    public String print(Date object, Locale locale) {
        return DateTimeFormatterUtil.formatToDateTimeStr(object);
    }
}
