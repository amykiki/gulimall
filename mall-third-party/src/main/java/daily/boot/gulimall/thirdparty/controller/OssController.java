package daily.boot.gulimall.thirdparty.controller;

import daily.boot.gulimall.common.utils.R;
import daily.boot.gulimall.thirdparty.service.AliOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("third/party/oss")
@Api(tags = "Oss第三方接口")
public class OssController {
    @Autowired
    private AliOssService ossService;
    
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件到Ali-Oss")
    public R uploadFile(@RequestParam("file") MultipartFile file) {
        String url = ossService.uplode(file);
        return R.ok().put("data", url);
    }
    
    @GetMapping("/policy")
    @ApiOperation(value = "获取OSS直传签名")
    public R getPolicy() {
        Map<String, String> policySign = ossService.policySign();
        return R.ok().put("data", policySign);
    }
    
    // TODO: 2020/10/22 回调需要在公网环境下进行，现在无法测试 
    @PostMapping("/callback")
    @ApiOperation(value = "OSS回调Callback")
    public Map<String, String> callback(@RequestBody String callbackBody,
                                        @RequestHeader HttpHeaders headers) {
        String autorizationInput = headers.getFirst("Authorization");
        String pubKeyUrl = headers.getFirst("x-oss-pub-key-url");
        Map<String, String> result= ossService.callback(autorizationInput, pubKeyUrl, callbackBody);
        return result;
    }
}
