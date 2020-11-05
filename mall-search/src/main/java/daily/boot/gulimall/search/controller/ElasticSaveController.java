package daily.boot.gulimall.search.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.search.entity.SkuEs;
import daily.boot.gulimall.search.service.SkuEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/search/save")
public class ElasticSaveController {
    
    @Autowired
    private SkuEsService skuEsService;
    
    /**
     * 上架商品
     */
    @PostMapping("/product")
    public Result<Boolean> productStatusUp(@RequestBody List<SkuEs> skuEsList) {
        boolean status = skuEsService.statusUp(skuEsList);
        return Result.ok(status);
    }
}
