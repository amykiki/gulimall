package daily.boot.gulimall.thirdparty.service.impl;

import daily.boot.gulimall.common.httpclient.*;
import daily.boot.gulimall.thirdparty.service.AliSmsService;
import daily.boot.gulimall.thirdparty.util.AliRequestConfig;
import daily.boot.gulimall.thirdparty.util.AliRequestSign;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AliSmsServiceImpl implements AliSmsService {
    private static final String SMS_HOST = "gyytz.market.alicloudapi.com";
    private static final Scheme SMS_SCHEME = Scheme.HTTP;
    private static final String SMS_PATH = "/sms/smsSend";
    //验证码：**code**，**minute**分钟内有效，您正在进行注册，若非本人操作，请勿泄露。
    private static final String SMS_REG_TEMPLATE = "a09602b817fd47e59e7c6e603d3f088d";
    @Autowired
    private HttpClientPool httpClientPool;
    @Value("${sms.smsSignId}")
    private String smsSignId;
    @Value("${sms.appKey}")
    private String appKey;
    @Value("${sms.appSecret}")
    private String appSecret;
    
    @Override
    public boolean sendRegVerifyCode(String phoneNum, String verifyCode, int validMin) {
        String templateParam = "**code**:" + verifyCode + ",**minute**:" + validMin;
        HttpClientReq clientReq = genSendRequest(phoneNum, SMS_REG_TEMPLATE, templateParam);
        HttpClientResp httpClientResp = httpClientPool.sendRequest(clientReq);
        if (httpClientResp != null && httpClientResp.getCode() == HttpStatus.SC_OK) {
            return true;
        }
        return false;
    }
    
    private HttpClientReq genSendRequest(String phoneNum, String templateId, String templateParam) {
        HttpClientReq clientReq = new HttpClientReq(HttpMethod.POST_FORM);
        clientReq.setHost(SMS_HOST);
        clientReq.setScheme(SMS_SCHEME);
        clientReq.setPath(SMS_PATH);
    
        clientReq.addParam("mobile" , phoneNum);
        clientReq.addParam("templateId" , templateId);
        clientReq.addParam("smsSignId" , smsSignId);
        clientReq.addParam("param" , templateParam);
    
        AliRequestConfig config = new AliRequestConfig(appKey, appSecret);
        AliRequestSign.addSignatureHeader(clientReq, config);
        
        return clientReq;
    }
}
