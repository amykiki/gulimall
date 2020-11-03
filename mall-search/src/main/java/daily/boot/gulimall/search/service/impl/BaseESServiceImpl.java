package daily.boot.gulimall.search.service.impl;

import daily.boot.gulimall.search.dao.ElasticSearchDao;
import daily.boot.gulimall.search.elasticsearch.annotation.ESDocument;
import daily.boot.gulimall.search.elasticsearch.annotation.ESId;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocHelper;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocInfo;
import daily.boot.gulimall.search.exception.ESErrorCode;
import daily.boot.gulimall.search.service.BaseESService;
import daily.boot.unified.dispose.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public abstract class BaseESServiceImpl<T> implements BaseESService<T>, InitializingBean {
    @Autowired
    protected ElasticSearchDao esDao;
    
    @Autowired
    private ElasticSearchConfig elasticSearchConfig;
    
    private ESDocInfo docInfo;
    
    /**
     * T对应的类型Class
     */
    protected Class<T> genericClass;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Class<T> beanClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), BaseESServiceImpl.class);
        genericClass = beanClass;
        docInfo = ESDocHelper.initTableInfo(beanClass, elasticSearchConfig);
    }
    
    @Override
    public IndexResponse saveOrUpdate(T genericInstance) {
        return esDao.saveOrUpdateRequest(docInfo.getIndexName(), getIdValue(genericInstance), genericInstance);
    }
    
    @Override
    public void createIndex() {
        boolean rtn = esDao.createIndexRequest(docInfo.getIndexName(), docInfo.getShards(), docInfo.isAutoExpandReplicas(), docInfo.getReplicas());
        if (rtn) {
            esDao.putMappingRequest(docInfo.getIndexName(), docInfo);
        }
        
    }
    
    /**
     * 获取当前操作的genericInstance的主键ID
     *
     * @param genericInstance 实例对象
     * @return 返回主键ID值
     */
    private String getIdValue(T genericInstance) {
        try {
            Object idValue = docInfo.getIdField().get(genericInstance);
            return idValue.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
