package daily.boot.gulimall.product.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
@EnableConfigurationProperties(ThreadPoolConfiguration.class)
public class MyThreadPoolConfig {
    
    @Bean(name = "productExecutor")
    public ExecutorService executorService(ThreadPoolConfiguration poolConfig) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("gulimall-product-thread-%d").build();
    
        return new ThreadPoolExecutor(poolConfig.getCoreSize(), poolConfig.getMaxSize(),
                               poolConfig.getKeepaliveTime(), TimeUnit.SECONDS,
                               new LinkedBlockingQueue<>(poolConfig.getMaxQueue()),
                               namedThreadFactory,
                               new ThreadPoolExecutor.AbortPolicy());
    }
}
