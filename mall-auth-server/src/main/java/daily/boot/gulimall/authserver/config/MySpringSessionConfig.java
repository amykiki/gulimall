package daily.boot.gulimall.authserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import daily.boot.gulimall.authserver.security.MySecurityJackson2Module;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 参考
 * SpringHttpSessionConfiguration
 * SessionAutoConfiguration --自动配置
 * RedisSessionConfiguration --自动配置
 * RedisIndexedSessionRepository
 * SessionRepositoryFilter -- 包裹对session请求的过滤器
 * SessionRepositoryFilterConfiguration -- 注册sessionRepositoryFilter
 */
@Configuration
@EnableRedisHttpSession
public class MySpringSessionConfig implements BeanClassLoaderAware {
    private ClassLoader loader;
    
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }
    
    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        objectMapper.registerModule(new MySecurityJackson2Module());
        return objectMapper;
    }
    
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }
}
