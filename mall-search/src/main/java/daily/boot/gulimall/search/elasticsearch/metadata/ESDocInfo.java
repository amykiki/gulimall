package daily.boot.gulimall.search.elasticsearch.metadata;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ES实体类反射信息
 */
@Data
@NoArgsConstructor
public class ESDocInfo {
    /**
     * 实体类型
     */
    private Class<?> entityType;
    /**
     * index名称
     */
    private String indexName;
    /**
     * 集群分片数目
     */
    private int shards;
    /**
     * 副本数目是否自动伸缩
     */
    private boolean autoExpandReplicas;
    /**
     * 如果为 autoExpandReplicas 为true，则replicas设置应为 0-1这样的形式
     * 如果为 autoExpandReplicas 为false，则replias设置为副本的具体数量
     */
    private String replicas;
    /**
     * ID字段
     */
    private Field idField;
    /**
     * 是否开启下划线转驼峰
     */
    private boolean underCamel;
    /**
     * ES字段反射信息列表
     */
    private List<ESFieldInfo> fieldList;
    
    public ESDocInfo(Class<?> entityType) {
        this.entityType = entityType;
    }
}
