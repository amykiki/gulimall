package daily.boot.gulimall.search.service.impl;

import daily.boot.gulimall.search.entity.SkuEs;
import daily.boot.gulimall.search.service.SkuEsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuEsServiceImpl extends BaseESServiceImpl<SkuEs> implements SkuEsService {
    @Override
    public boolean statusUp(List<SkuEs> skuEsList) {
        return false;
    }
}
