package daily.boot.gulimall.thirdparty.util;

import com.alibaba.cloudapi.sdk.signature.HMacSHA256SignerFactory;
import lombok.Data;

import java.util.Date;

@Data
public class AliRequestConfig {
    private String appKey;
    private String appSecret;
    private Date currentDate;
    private boolean isGenerateContentMd5 = true;
    private boolean isGenerateNonce = true;
    private boolean isNeedSignature = true;
    private String signatureMethod = HMacSHA256SignerFactory.METHOD;
    
    public AliRequestConfig() {
    }
    
    public AliRequestConfig(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
}
