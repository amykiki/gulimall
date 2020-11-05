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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
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
     * 默认true会检索该字段
     * false 不检索该字段
     * @return
     */
    private final boolean index;
    /**
     * 倒排索引可以提供全文检索能力，但是无法提供对排序和数据聚合的支持。
     * doc_value采用了面向列的存储方式存储一个field的内容，
     * 可以实现高效的排序和聚合。
     * 默认情况下，ES几乎会为所有类型的字段存储doc_value
     * 如果不需要对某个字段进行排序或者聚合，则可以关闭该字段的doc_value存储
     * @return
     */
    private final boolean docValues;
    /**
     * es字段名
     */
    private final String esFieldName;
    
    public ESFieldInfo(Field field, ElasticSearchConfig esConfig) {
        String esFieldName = null; //没有额外注解时，默认使用
        ESFieldType esFieldType = null;
        String analyzer = null;
        boolean index = true;
        boolean docValues = true;
    
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
            //index
            index = esField.getBoolean("index");
            docValues = esField.getBoolean("docValues");
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
        this.index = index;
        this.docValues = docValues;
        this.field = field;
        this.property = field.getName();
        this.propertyType = findPropertyType(field, esConfig);
    }
    
    private Class<?> findPropertyType(Field curFiedld, ElasticSearchConfig esConfig) {
        if (Collection.class.isAssignableFrom(curFiedld.getType())) {
            Type genericType = curFiedld.getGenericType();
            if (Objects.isNull(genericType)) {
                return null;
            }
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                // 如果是自定义的类，需要获取该类的ESDocInfo
                Class<?> clazz = (Class<?>) pt.getActualTypeArguments()[0];
                ESDocHelper.initTableInfo(clazz, esConfig);
                // 得到泛型里的class类型对象
                return clazz;
            }
            return null;
        }else {
            return curFiedld.getType();
        }
    }
}
