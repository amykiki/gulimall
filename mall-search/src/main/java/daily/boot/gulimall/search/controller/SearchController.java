package daily.boot.gulimall.search.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = "检索页")
public class SearchController {
    
    @GetMapping("/list.html")
    public String listPage() {
        return "list";
    }
}
