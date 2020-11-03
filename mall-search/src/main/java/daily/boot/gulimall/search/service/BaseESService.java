package daily.boot.gulimall.search.service;

import org.elasticsearch.action.index.IndexResponse;

public interface BaseESService<T> {
    
    /**
     * 保存或更新文档数据
     *
     * @param genericInstance 文档数据集合
     */
    IndexResponse saveOrUpdate(T genericInstance);
    
    /**
     * 创建实体类T相关Index
     */
    void createIndex();
}
