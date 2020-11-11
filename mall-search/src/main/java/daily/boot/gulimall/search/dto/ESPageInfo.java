package daily.boot.gulimall.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ESPageInfo<T> implements Serializable {
    private static final long serialVersionUID = 2463153190547412323L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 当前页数
     */
    private int pageNum;
    /**
     * 列表数据 和 高亮数据
     */
    private List<EntityWithHighlight<T>> list;
    private Aggregations aggregations;
    
    public ESPageInfo(long total, int pageSize, int totalPages, int pageNum, List<EntityWithHighlight<T>> list, Aggregations aggregations) {
        this.total = total;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.pageNum = pageNum;
        this.list = list;
        this.aggregations = aggregations;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityWithHighlight<T> implements Serializable{
    
        private static final long serialVersionUID = 8834672366858716889L;
        private T entity;
        private Map<String, HighlightField> highlightFields;
    }
}
