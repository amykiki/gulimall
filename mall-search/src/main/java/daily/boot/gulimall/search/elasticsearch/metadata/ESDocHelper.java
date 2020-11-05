package daily.boot.gulimall.search.elasticsearch.metadata;

import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.common.utils.ReflectionUtil;
import daily.boot.gulimall.search.elasticsearch.annotation.ESDocument;
import daily.boot.gulimall.search.elasticsearch.annotation.ESField;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.utils.StringExtUtils;
import daily.boot.gulimall.search.exception.ESErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ES实体类反射辅助类
 */
@Slf4j
public class ESDocHelper {
    private static final Map<Class<?>, ESDocInfo> ESDOC_INFO_CACHE = new ConcurrentHashMap<>();
    
    /**
     * <p>
     * 实体类反射获取ES信息【初始化】
     * </p>
     *
     * @param clazz 反射实体类
     * @return ES反射信息
     */
    public synchronized static ESDocInfo initTableInfo(Class<?> clazz, ElasticSearchConfig esConfig) {
        boolean match = esConfig.getDocLocation().stream().anyMatch(location -> clazz.getName().contains(location));
        if (!match) {
            return null;
        }
        ESDocInfo esDocInfo = ESDOC_INFO_CACHE.get(clazz);
        if (Objects.nonNull(esDocInfo)) {
            return esDocInfo;
        }
        //初始化Index相关
        esDocInfo = new ESDocInfo(clazz);
        esDocInfo.setUnderCamel(esConfig.isMapUnderscoreToCamelCase());
        
        initDocInfo(clazz, esConfig, esDocInfo);
    
        /* 初始化字段相关 */
        initESFields(clazz, esDocInfo, esConfig);
    
        //放入缓存
        ESDOC_INFO_CACHE.put(clazz, esDocInfo);
        
        return esDocInfo;
    }
    
    public static ESDocInfo getEsDocInfo(Class<?> clazz) {
        return ESDOC_INFO_CACHE.get(clazz);
    }
    
    private static void initESFields(Class<?> clazz, ESDocInfo esDocInfo, ElasticSearchConfig esConfig) {
        //获取所有属性字段，并过滤被ESField.exist = false的属性
        List<Field> list = getAllFields(clazz);
        //获取主键field
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ESId esId = declaredField.getAnnotation(ESId.class);
            if (Objects.nonNull(esId)) {
                esDocInfo.setIdField(declaredField);
                esDocInfo.getIdField().setAccessible(true);
                break;
            }
        }
        /* 未发现主键注解，提示警告信息 */
        if (Objects.isNull(esDocInfo.getIdField())) {
            throw new BusinessException(ESErrorCode.ES_ENTITY_ERROR, "ESId注解未指定，主键为空");
        }
        List<ESFieldInfo> esFieldInfoList = list.stream().map(field -> new ESFieldInfo(field, esConfig)).collect(Collectors.toList());
    
        /* 字段列表,不可变集合 */
        esDocInfo.setFieldList(Collections.unmodifiableList(esFieldInfoList));
    }
    
    private static void initDocInfo(Class<?> clazz, ElasticSearchConfig esConfig, ESDocInfo esDocInfo) {
        ESDocument esDocument = AnnotationUtils.findAnnotation(clazz, ESDocument.class);
        String indexName = clazz.getSimpleName();
        Integer shards = esConfig.getIndex().getNumberOfShards();
        String replicas = esConfig.getIndex().getNumberOfReplicas();
        boolean autoExpandReplicas = esConfig.getIndex().isAutoExpandReplicas();
        
        if (esDocument != null) {
            if (StringUtils.isNotBlank(esDocument.indexName())) {
                indexName = esDocument.indexName();
            } else {
                indexName = initIndexNameWithESConfig(indexName, esConfig);
            }
            if (esDocument.shards() > 0) {
                shards = esDocument.shards();
            }
            if (StringUtils.isNotBlank(esDocument.replicas())) {
                autoExpandReplicas = esDocument.autoExpandReplicas();
                replicas = esDocument.replicas();
            }
            
        } else {
            indexName = initIndexNameWithESConfig(indexName, esConfig);
        }
        esDocInfo.setIndexName(indexName);
        esDocInfo.setShards(shards);
        esDocInfo.setReplicas(replicas);
        esDocInfo.setAutoExpandReplicas(autoExpandReplicas);
    }
    
    private static String initIndexNameWithESConfig(String indexName, ElasticSearchConfig esConfig) {
        // 开启index名下划线申明
        if (esConfig.isIndexUnderline()) {
            indexName = StringExtUtils.camelToUnderline(indexName);
        }
        
        //首字母小写
        indexName = StringExtUtils.firstToLowerCase(indexName);
        return indexName;
    }
    
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = ReflectionUtil.getFieldList(clazz);
        return fieldList.stream().filter(
                field -> {
                    ESField esField = field.getAnnotation(ESField.class);
                    return esField == null || esField.exist();
                }
        ).collect(Collectors.toList());
    }
    
    
}
