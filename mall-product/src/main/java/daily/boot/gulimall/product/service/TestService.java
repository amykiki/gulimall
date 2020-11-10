package daily.boot.gulimall.product.service;

public interface TestService {
    String getSmsCode(String phone);
    
    String verifySmsCode(String phone, String code);
    
    void clearAllSmsCodes();
}
