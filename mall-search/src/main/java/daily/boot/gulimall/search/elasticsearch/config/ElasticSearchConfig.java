package daily.boot.gulimall.search.elasticsearch.config;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "gulimall.search.es")
public class ElasticSearchConfig implements InitializingBean {
    /**
     * es实体类转json是否驼峰转下划线
     */
    private boolean mapUnderscoreToCamelCase;
    /**
     * indexName是否使用下划线命名（默认 true:默认es-index下划线命名）
     */
    private boolean indexUnderline = true;
    /**
     * es实体类扫描包路径
     */
    private List<String> docLocation;
    
    /**
     * 索引配置信息
     * 默认numberOfShards为3
     * 默认numberOfReplicas为2
     * 默认autoExpandReplicas为false
     */
    private Index index = new Index();
    
    private SerializeConfig config = new SerializeConfig();
    private ParserConfig parserConfig = new ParserConfig();
    
    @Override
    public void afterPropertiesSet() throws Exception {
        //判断，如果使用了下划线转驼峰，则修改FASTJSON的配置
        if (mapUnderscoreToCamelCase) {
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        }
    }
    
    /**
     * 索引配置信息
     */
    @Data
    public static class Index {
        
        /**
         * 分片数量
         */
        private Integer numberOfShards = 3;
        
        /**
         * 副本数量
         */
        private String numberOfReplicas = "2";
        private boolean autoExpandReplicas;
        
    }
    
}
