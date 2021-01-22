package daily.boot.gulimall.member.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

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
public class MySpringSessionConfig {
    @Value("${server.servlet.session.cookie.name}")
    private String cookieName;
    
    @Bean
    public CookieSerializer cookieSerializer() {
        
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        
        //放大作用域
        /*
         注意，显示指定了cookenDomainName，在生成的cookie中，domainName最后前面会有一个点
         如本例中为 .cart.gulimall.com
         参考 https://www.cnblogs.com/ChineseLiao/p/12765323.html
             https://segmentfault.com/a/1190000019548907
             https://stackoverflow.com/questions/9618217/what-does-the-dot-prefix-in-the-cookie-domain-mean
             domain可以允许的属性值总结如下：
             1 如果显式规定，属性值只能是当前域或者其父域，此时浏览器会自动给域或者父域前面加点。
             2 如果省略此属性，那么domain属性就是当前页面所在的域（前面不会自动加点）。

             domain属性规定域的作用范围：
             1 如果属性值最终结果带有点，那么作用范围是最终结果这个域本身或者其所有子域。
             2 如果属性值最终结果没有点，那么作用范围是最终结果这个域本身。
             
             但是根据Stack Overflow，如果是新的浏览器，前面带不带点已经无所谓了，都会传给子域，但是老的浏览器必须带点才能给子域
             The leading dot means that the cookie is valid for subdomains as well;
             nevertheless recent HTTP specifications (RFC 6265) changed this rule
             so modern browsers should not care about the leading dot.
             The dot may be needed by old browser implementing the deprecated RFC 2109.
         */
        cookieSerializer.setDomainName("member.gulimall.com");
        cookieSerializer.setCookieName(cookieName);
        
        return cookieSerializer;
    }
    
    //@Bean
    //public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    //    return new GenericJackson2JsonRedisSerializer(objectMapper());
    //}
    
    //private ObjectMapper objectMapper() {
    //    ObjectMapper objectMapper = new ObjectMapper();
    //    objectMapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
    //    objectMapper.registerModule(new MySecurityJackson2Module());
    //    return objectMapper;
    //}
    
    
}
