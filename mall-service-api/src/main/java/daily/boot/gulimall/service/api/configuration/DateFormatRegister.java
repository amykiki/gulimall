package daily.boot.gulimall.service.api.configuration;

import daily.boot.gulimall.service.api.converter.Date2StringConverter;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.Date;

public class DateFormatRegister implements FeignFormatterRegistrar {
    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(Date.class, String.class, new Date2StringConverter());
    }
}
