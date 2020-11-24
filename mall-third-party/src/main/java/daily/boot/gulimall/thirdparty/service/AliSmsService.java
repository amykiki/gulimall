package daily.boot.gulimall.thirdparty.service;

/**
 * 短信发送服务
 */
public interface AliSmsService {
    boolean sendRegVerifyCode(String phoneNum, String verifyCode, int validMin);
}
