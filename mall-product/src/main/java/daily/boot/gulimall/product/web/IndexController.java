package daily.boot.gulimall.product.web;

import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.CategoryService;
import daily.boot.gulimall.product.vo.Catelog2Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "首页")
public class IndexController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {
        //查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories", categoryEntities);
        return "index";
    }

    //index/json/catalog.json
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "hello" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }

}
