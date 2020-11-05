package daily.boot.gulimall.search.service;

import daily.boot.gulimall.search.entity.SkuEs;

import java.util.List;

public interface SkuEsService extends BaseESService<SkuEs> {
    boolean statusUp(List<SkuEs> skuEsList);
}
