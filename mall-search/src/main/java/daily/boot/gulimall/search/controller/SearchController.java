package daily.boot.gulimall.search.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.search.service.SkuEsService;
import daily.boot.gulimall.search.vo.SearchParam;
import daily.boot.gulimall.search.vo.SkuSearchResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "检索页")
public class SearchController {
    @Autowired
    private SkuEsService skuEsService;
    
    @ApiOperation("检索页面")
    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        //url = request.getScheme() +"://" + request.getServerName()
        //      + ":" +request.getServerPort()
        //      + request.getServletPath();
        String url = "http://search.gulimall.com/list.html";
        searchParam.setReqUrl(url);
        String queryString = request.getQueryString();
        SkuSearchResult skuSearchResult = skuEsService.skuSearch(searchParam, queryString);
        model.addAttribute("result", skuSearchResult);
        return "list";
    }
    
    @ApiOperation("检索测试")
    @GetMapping("/searchTest")
    @ResponseBody
    public Result<SkuSearchResult> searchTest(SearchParam searchParam, HttpServletRequest request) {
        return Result.ok(skuEsService.skuSearch(searchParam, request.getQueryString()));
    }
}
