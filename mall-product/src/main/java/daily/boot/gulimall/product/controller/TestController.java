package daily.boot.gulimall.product.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.service.*;
import daily.boot.gulimall.product.vo.Catelog2Vo;
import daily.boot.gulimall.product.vo.SkuItemVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("test")
@Api(tags = "测试接口")
public class TestController {
    @Autowired
    private TestService testService;
    
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    
    @GetMapping("/smscode/{phone}")
    public String getSmsCode(@PathVariable(value = "phone") String phone) {
        return testService.getSmsCode(phone);
    }
    
    @PostMapping("/smscode/verify")
    public String verifySmsCode(String phone, String smsCode) {
        return testService.verifySmsCode(phone, smsCode);
    }
    
    @PostMapping("/smscode/clearall")
    public String clearAllSmsCode() {
        testService.clearAllSmsCodes();;
        return "cleared";
    }
    
    @GetMapping("/skuinfo/{skuId}")
    public Result getSkuInfo(@PathVariable("skuId") Long skuId) {
        //SkuInfoEntity byId = skuInfoService.getById(skuId);
        SkuItemVo rtn = skuInfoService.item(skuId);
        //Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return Result.ok(rtn);
    }
}
