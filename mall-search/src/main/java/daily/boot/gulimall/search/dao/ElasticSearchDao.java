package daily.boot.gulimall.search.dao;

import com.alibaba.fastjson.JSON;
import daily.boot.gulimall.search.elasticsearch.config.ElasticSearchConfig;
import daily.boot.gulimall.search.elasticsearch.enums.ESFieldType;
import daily.boot.gulimall.search.elasticsearch.metadata.ESDocInfo;
import daily.boot.gulimall.search.elasticsearch.metadata.ESFieldInfo;
import daily.boot.gulimall.search.exception.ESErrorCode;
import daily.boot.unified.dispose.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class ElasticSearchDao{
    
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
                builder.startObject("properties");
                {
                    for (ESFieldInfo esFieldInfo : esDocInfo.getFieldList()) {
                        builder.startObject(esFieldInfo.getEsFieldName());
                        {
                            builder.field("type", esFieldInfo.getEsFieldType().typeName);
                            String analyzer = esFieldInfo.getAnalyzer();
                            if (StringUtils.isNotBlank(analyzer)) {
                                builder.field("analyzer", analyzer);
                            }
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
                        }
                        builder.endObject();
                    }
                }
                builder.endObject();
            }
            builder.endObject();
            request.source(builder);
            AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
            log.info("PutMapping -- acknowledged : :{}", putMappingResponse.isAcknowledged());
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
            throw new BusinessException(ESErrorCode.ES_DOC_ERROR, "更新索引文档 {" + indexName + "} 数据 {" + object + "} 失败");
        }
    }
    
    public void deleteRequest(String indexName, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
            client.delete(deleteRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_DOC_ERROR, "删除索引文档 {" + indexName + "} 数据id {" + id + "} 失败");
        }
    }
    
    public IndexResponse saveOrUpdateRequest(String indexName, String id, Object instance) {
        try {
            IndexRequest request = new IndexRequest(indexName).id(id).source(JSON.toJSONString(instance, esConfig.getConfig()), XContentType.JSON);
            IndexResponse response = client.index(request, COMMON_OPTIONS);
            log.info("saveOrUpdateRequest -- IndexResponse : :{}",response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ESErrorCode.ES_DOC_ERROR, "创建/更新索引文档 {" + indexName + "} 数据id {" + id + "} 数据instance {" + instance + "} 失败");
        }
    }
    
    
}
