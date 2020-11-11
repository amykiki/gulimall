package daily.boot.gulimall.search.service;

import daily.boot.gulimall.search.entity.SkuEs;
import daily.boot.gulimall.search.vo.SearchParam;
import daily.boot.gulimall.search.vo.SkuSearchResult;

import java.util.List;

public interface SkuEsService extends BaseESService<SkuEs> {
    boolean statusUp(List<SkuEs> skuEsList);
    
    /**
     *
     * @param param
     * @param queryString 用来做面包屑导航，生成连接用
     * @return
     */
    SkuSearchResult skuSearch(SearchParam param, String queryString);
}
