package daily.boot.gulimall.seckill.config;

import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class MyRedissonConfig {
    
    private String host;
    private String port;
    private String password;
    private Integer database;
    
    /**
     * 所有对Redisson的使用都是通过RedissoonClient
     * @return
     */
    @Bean
    public RedissonClient redissonClient() {
        //1. 创建配置
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://" + host + ":" + port)
              .setPassword(password)
              .setDatabase(database);
        
        //2. 根据configg创建出RedissonClient实例
        return Redisson.create(config);
    }
}
