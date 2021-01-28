package daily.boot.gulimall.service.api.configuration;

import daily.boot.gulimall.service.api.formatter.DateFormatter;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class String2DateFormatRegister implements FeignFormatterRegistrar {
    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter());
    }
}
