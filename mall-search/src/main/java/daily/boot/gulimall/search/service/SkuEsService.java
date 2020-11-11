package daily.boot.gulimall.search.service;

import daily.boot.gulimall.search.dto.ESPageInfo;
import daily.boot.gulimall.search.entity.SkuEs;
import daily.boot.gulimall.search.vo.SearchParam;
import daily.boot.gulimall.search.vo.SkuSearchResult;

import java.util.List;

public interface SkuEsService extends BaseESService<SkuEs> {
    boolean statusUp(List<SkuEs> skuEsList);
    
    SkuSearchResult skuSearch(SearchParam param);
}
