package daily.boot.gulimall.search.dao;

import com.alibaba.fastjson.JSON;
import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocHelper;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocInfo;
import daily.boot.gulimall.search.elasticsearch.metadata.ESFieldInfo;
import daily.boot.gulimall.search.exception.ESErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ElasticSearchDao {
    
    private static final int ES_SHARDS = 1;
    private static final String ES_REPLICAS = "0";
    @Autowired
    private RestHighLevelClient client;
    
    @Resource(name = "elasticSearchConfig")
    private ElasticSearchConfig esConfig;
    
    
    public static final RequestOptions COMMON_OPTIONS;
    
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        
        // 默认缓冲限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }
    
    public boolean existIndex(String indexName) {
        boolean exists = false;
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_INDEX_ERROR, "判断索引 {" + indexName + "} 是否存在失败");
            
        }
        return exists;
    }
    
    public void createIndexRequest(String indexName) {
        createIndexRequest(indexName, ES_SHARDS, false, ES_REPLICAS);
    }
    
    public boolean createIndexRequest(String indexName, int shards, boolean autoExpandReplicas, String replicas) {
        if (existIndex(indexName)) {
            return false;
        }
        
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                                     .put("index.number_of_shards", shards)
                                     .put(autoExpandReplicas
                                          ? "index.auto_expand_replicas"
                                          : "index.number_of_replicas", replicas));
            log.info("CreateIndexRequest---indexName[{}], autoExpandReplicas[{}], replicas[{}]", indexName, autoExpandReplicas, replicas);
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            log.info(" acknowledged : {}", createIndexResponse.isAcknowledged());
            log.info(" shardsAcknowledged :{}", createIndexResponse.isShardsAcknowledged());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_INDEX_ERROR, "创建索引 {" + indexName + "} 失败");
        }
    }
    
    public void putMappingRequest(String indexName, ESDocInfo esDocInfo) {
        if (CollectionUtils.isEmpty(esDocInfo.getFieldList())) {
            return;
        }
        
        try {
            PutMappingRequest request = new PutMappingRequest(indexName);
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                buildDocMapping(builder, esDocInfo);
            }
            builder.endObject();
            request.source(builder);
            AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
            log.debug("PutMapping -- acknowledged : :{}", putMappingResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_MAPPING_ERROR, "putMappingRequest方法异常");
        }
    }
    
    public void deleteIndexRequest(String indexName) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_INDEX_ERROR, "删除索引 {" + indexName + "} 失败");
        }
    }
    
    public void updateRequest(String indexName, String id, Object object) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, id)
                    .doc(JSON.toJSONString(object, esConfig.getConfig()), XContentType.JSON);
            client.update(updateRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_UPDATE_ERROR, "更新索引文档 {" + indexName + "} 数据 {" + object + "} 失败");
        }
    }
    
    public void deleteRequest(String indexName, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
            client.delete(deleteRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_DELETE_ERROR, "删除索引文档 {" + indexName + "} 数据id {" + id + "} 失败");
        }
    }
    
    public IndexResponse saveOrUpdateRequest(String indexName, String id, Object instance) {
        try {
            IndexRequest request = new IndexRequest(indexName).id(id).source(JSON.toJSONString(instance, esConfig.getConfig()), XContentType.JSON);
            IndexResponse response = client.index(request, COMMON_OPTIONS);
            log.debug("saveOrUpdateRequest -- IndexResponse : :{}", response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_DOC_ERROR, "创建/更新索引文档 {" + indexName + "} 数据id {" + id + "} 数据instance {" + instance + "} 失败");
        }
    }
    
    public SearchResponse search(String indexName, SearchSourceBuilder sourceBuilder) {
        try {
            Assert.notNull(sourceBuilder, "search-SourceBuilder不能为空");
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, COMMON_OPTIONS);
            log.debug("search -- searchResponse:{}", searchResponse);
            return searchResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_SEARCH_ERROR, "查询索引文档 {" + sourceBuilder + "}异常-->" + e.getMessage());
        }
    }
    
    private void buildDocMapping(XContentBuilder builder, ESDocInfo esDocInfo) throws IOException {
        if (Objects.isNull(esDocInfo) || CollectionUtils.isEmpty(esDocInfo.getFieldList())) {
            return;
        }
        
        builder.startObject("properties");
        {
            for (ESFieldInfo esFieldInfo : esDocInfo.getFieldList()) {
                builder.startObject(esFieldInfo.getEsFieldName());
                {
                    //分词器设置
                    String analyzer = esFieldInfo.getAnalyzer();
                    if (StringUtils.isNotBlank(analyzer)) {
                        builder.field("analyzer", analyzer);
                    }
                    //是否索引
                    if (!esFieldInfo.isIndex()) {
                        builder.field("index", false);
                    }
                    //排序和数据聚合支持
                    if (!esFieldInfo.isDocValues()) {
                        builder.field("doc_values", false);
                    }
                    //类型
                    builder.field("type", esFieldInfo.getEsFieldType().typeName);
                    //类型为Text，增加默认的keyword字段
                    if (ESFieldType.Text == esFieldInfo.getEsFieldType()) {
                        builder.startObject("fields");
                        {
                            builder.startObject("keyword");
                            {
                                builder.field("type", "keyword");
                                builder.field("ignore_above", 256);
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    //类型为nested，递归写
                    if (ESFieldType.Nested == esFieldInfo.getEsFieldType()) {
                        Class<?> nestedClazz = esFieldInfo.getPropertyType();
                        ESDocInfo nestedDocInfo = ESDocHelper.getEsDocInfo(nestedClazz);
                        buildDocMapping(builder, nestedDocInfo);
                    }
                }
                builder.endObject();
            }
        }
        builder.endObject();
    }
    
    public boolean bulkSaveOrUpdate(List list, String indexName, Field idField) {
        BulkRequest bulkRequest = null;
        try {
            bulkRequest = new BulkRequest();
            for (Object instance : list) {
                bulkRequest.add(new IndexRequest(indexName)
                                             .id(getIdValue(idField, instance))
                                             .source(JSON.toJSONString(instance, esConfig.getConfig()), XContentType.JSON));
            }
           
            log.debug("bulkSaveOrUpdate -- bulkRequest : :{}", bulkRequest);
            BulkResponse bulkResponse = client.bulk(bulkRequest, COMMON_OPTIONS);
            List<String> succussIds = new ArrayList<>();
            boolean hasFailures = false;
            for (BulkItemResponse resp : bulkResponse) {
                if (resp.isFailed()) {
                    BulkItemResponse.Failure failure = resp.getFailure();
                    log.warn("bulkSaveOrUpdate -- 批量失败，失败信息:{}", failure);
                    hasFailures = true;
                } else {
                    succussIds.add(resp.getId());
                }
            }
            return !hasFailures;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_BULK_ERROR, "批量创建/更新索引文档 {" + indexName + "} bulkRequest {" + bulkRequest + "} 失败");
        }
        
    }
    
    /**
     * 获取当前操作的genericInstance的主键ID
     *
     * @param object 实例对象
     * @return 返回主键ID值
     */
    private String getIdValue(Field idField, Object object) {
        try {
            Object idValue = idField.get(object);
            return idValue.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
