package daily.boot.gulimall.search.elasticsearch.metadata;

import com.alibaba.nacos.common.utils.StringUtils;
import daily.boot.gulimall.search.elasticsearch.annotation.ESField;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import daily.boot.gulimall.search.elasticsearch.utils.StringExtUtils;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * ES实体类字段反射信息
 */
@Data
public class ESFieldInfo {
    private final Field field;
    /**
     * 实体字段名
     */
    private final String property;
    private final Class<?> propertyType;
    private final ESFieldType esFieldType;
    /**
     * 分词器
     */
    private final String analyzer;
    /**
     * es字段名
     */
    private final String esFieldName;
    
    public ESFieldInfo(Field field, ElasticSearchConfig esConfig) {
        String esFieldName = null; //没有额外注解时，默认使用
        ESFieldType esFieldType = null;
        String analyzer = null;
    
        AnnotationAttributes esField = AnnotatedElementUtils.getMergedAnnotationAttributes(field, ESField.class);
        if (Objects.nonNull(esField)) {
            String name = esField.getString("name");
            if (StringUtils.isNotBlank(name)) {
                esFieldName = name;
            } else {
                /* 开启字段下划线申明 */
                if (esConfig.isMapUnderscoreToCamelCase()) {
                    esFieldName = StringExtUtils.camelToUnderline(esFieldName);
                }
            }
            ESFieldType type = (ESFieldType) esField.get("type");
            if (Objects.nonNull(type)) {
                esFieldType = type;
            }
            //分词器
            if (StringUtils.isNotBlank(esField.getString("analyzer"))) {
                analyzer = esField.getString("analyzer");
            }
        }
        if (StringUtils.isBlank(esFieldName)) {
            esFieldName = field.getName();
            if (esConfig.isMapUnderscoreToCamelCase()) {
                esFieldName = StringExtUtils.camelToUnderline(esFieldName);
            }
        }
    
        if (Objects.isNull(esFieldType)) {
            esFieldType = ESTypeHandlerRegistry.getESFieldType(field.getType());
        }
        
        this.esFieldName = esFieldName;
        this.esFieldType = esFieldType;
        this.analyzer = analyzer;
        this.field = field;
        this.property = field.getName();
        this.propertyType = field.getType();
    }
    
}
