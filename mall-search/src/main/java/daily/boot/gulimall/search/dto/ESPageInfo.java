package daily.boot.gulimall.search.dto;

import lombok.Data;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import java.io.Serializable;
import java.util.List;

@Data
public class ESPageInfo<T> implements Serializable {
    private static final long serialVersionUID = 2463153190547412323L;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage;
    /**
     * 列表数据
     */
    private List<T> list;
    private Aggregations aggregations;
}
