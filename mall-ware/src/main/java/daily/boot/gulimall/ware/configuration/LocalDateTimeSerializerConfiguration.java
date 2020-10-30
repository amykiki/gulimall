package daily.boot.gulimall.ware.configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import daily.boot.common.util.DateTimeFormatterUtil;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
/**
 * LocalDateTime/LocalDate/LocalTime前台json格式化
 */
public class LocalDateTimeSerializerConfiguration {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT))
                                 .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatterUtil.YYYY_MM_DD_FMT))
                                 .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatterUtil.HH_MM_SS_FMT));
    }
}
