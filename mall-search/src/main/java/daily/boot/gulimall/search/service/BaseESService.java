package daily.boot.gulimall.search.service;

import daily.boot.gulimall.search.dto.ESPageInfo;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

public interface BaseESService<T> {
    
    /**
     * 保存或更新文档数据
     *
     * @param genericInstance 文档数据集合
     */
    IndexResponse saveOrUpdate(T genericInstance);
    
    boolean bulkSaveOrUpdate(List<T> list);
    
    /**
     * 创建实体类T相关Index
     */
    void createIndex();
    
    /**
     * 搜索文档，根据指定的搜索条件
     *
     * @param searchSourceBuilder
     * @return
     */
    ESPageInfo<T> search(SearchSourceBuilder searchSourceBuilder);
}
