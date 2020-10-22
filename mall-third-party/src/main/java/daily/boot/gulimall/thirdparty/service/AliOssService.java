package daily.boot.gulimall.thirdparty.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AliOssService {
    
    String uplode(MultipartFile file);
    
    Map<String, String> policySign();
    
    /**
     * 回调处理
     */
    Map<String, String> callback(String autorizationInput, String pubKeyUrl, String callbackBody);
}
