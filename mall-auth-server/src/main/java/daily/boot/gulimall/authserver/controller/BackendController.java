package daily.boot.gulimall.authserver.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.authserver.security.OAuthClientDetails;
import daily.boot.gulimall.authserver.service.impl.OAuthClientDetailsServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("后端管理-BackendController")
@RestController
@RequestMapping("/backend")
public class BackendController {
    @Autowired
    private OAuthClientDetailsServiceImpl clientDetailsService;
    
    @PostMapping("/add")
    public Result<Boolean> addOAuthClient(@RequestBody OAuthClientDetails clientDetails) {
        clientDetailsService.save(clientDetails);
        return Result.ok(true);
    }
    
    @GetMapping("/loadClient")
    public Result<OAuthClientDetails> loadOAuthClient(String clientId) {
        OAuthClientDetails clientDetails = (OAuthClientDetails)clientDetailsService.loadClientByClientId(clientId);
        return Result.ok(clientDetails);
    }
}
