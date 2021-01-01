package daily.boot.gulimall.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gulimall.threadpool")
@Data
public class ThreadPoolConfiguration {
    private Integer coreSize = 10;
    private Integer maxSize = 30;
    private Long keepaliveTime = 30L; //单位为秒
    private Integer maxQueue = 100;
}
