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
    
    /**
     * 默认true会检索该字段
     * false 不检索该字段
     * @return
     */
    boolean index() default true;
    
    /**
     * 倒排索引可以提供全文检索能力，但是无法提供对排序和数据聚合的支持。
     * doc_value采用了面向列的存储方式存储一个field的内容，
     * 可以实现高效的排序和聚合。
     * 默认情况下，ES几乎会为所有类型的字段存储doc_value
     * 如果不需要对某个字段进行排序或者聚合，则可以关闭该字段的doc_value存储
     * @return
     */
    boolean docValues() default true;
}
