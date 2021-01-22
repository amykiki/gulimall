package daily.boot.gulimall.order.configuration;

import daily.boot.common.util.DateTimeFormatterUtil;

import java.beans.PropertyEditorSupport;
import java.util.Date;

public class CustomDateEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        return DateTimeFormatterUtil.formatToDateTimeStr((Date) getValue());
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(DateTimeFormatterUtil.parseToDate(text, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
    }
}
