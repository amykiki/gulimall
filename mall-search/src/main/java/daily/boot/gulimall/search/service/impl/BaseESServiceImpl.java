package daily.boot.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import daily.boot.gulimall.search.dao.ElasticSearchDao;
import daily.boot.gulimall.search.dto.ESPageInfo;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocHelper;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocInfo;
import daily.boot.gulimall.search.service.BaseESService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public boolean bulkSaveOrUpdate(List<T> list) {
        return esDao.bulkSaveOrUpdate(list, docInfo.getIndexName(), docInfo.getIdField());
    }
    
    @Override
    public void createIndex() {
        boolean rtn = esDao.createIndexRequest(docInfo.getIndexName(), docInfo.getShards(), docInfo.isAutoExpandReplicas(), docInfo.getReplicas());
        if (rtn) {
            esDao.putMappingRequest(docInfo.getIndexName(), docInfo);
        }
        
    }
    
    @Override
    public ESPageInfo<T> search(SearchSourceBuilder searchSourceBuilder) {
        SearchResponse searchResponse = esDao.search(docInfo.getIndexName(), searchSourceBuilder);
        if (Objects.isNull(searchResponse)) {
            return null;
        }
        ESPageInfo<T> info = new ESPageInfo<>();
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        //获取查询结果
        List<ESPageInfo.EntityWithHighlight<T>> entities = Arrays.stream(hits).map(hit -> {
            String sourceAsString = hit.getSourceAsString();
            T t = JSON.parseObject(sourceAsString, genericClass);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            ESPageInfo.EntityWithHighlight<T> e = new ESPageInfo.EntityWithHighlight<>(t, highlightFields);
            return e;
        }).collect(Collectors.toList());
        info.setList(entities);
    
        //总查询结果
        info.setTotal(searchHits.getTotalHits().value);
        
        //汇总结果
        info.setAggregations(searchResponse.getAggregations());
        return info;
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
