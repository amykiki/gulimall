package daily.boot.gulimall.search.elasticsearch.annotation;

import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * https://github.com/zhangboqing/spring-boot-demo-elasticsearch-rest-high-level-client/blob/568d85beefde59c29d9031f794b5e0b7f89d4722/src/main/java/com/zbq/springbootelasticsearch/common/elasticsearch/annotation/ESField.java
 * @author zhangboqing
 * @date 2019/12/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ESField {
    @AliasFor("name")
    String value() default "";
    
    @AliasFor("value")
    String name() default "";
    
    ESFieldType type();
    
    /**
     * 是否为es表字段
     * 默认 true 存在，false 不存在
     */
    boolean exist() default true;
    
    String analyzer() default "";
}
