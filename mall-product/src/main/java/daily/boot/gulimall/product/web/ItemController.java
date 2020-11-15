package daily.boot.gulimall.product.web;

import daily.boot.gulimall.product.service.SkuInfoService;
import daily.boot.gulimall.product.vo.SkuItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Api(tags = "详情页")
public class ItemController {
    @Autowired
    private SkuInfoService skuInfoService;
    
    @GetMapping("/{skuId:\\d+}.html")
    @ApiOperation("当前SKU详情")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
        SkuItemVo itemVo = skuInfoService.item(skuId);
        model.addAttribute("item", itemVo);
        return "item";
    }
}
