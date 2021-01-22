package daily.boot.gulimall.member.security;

import org.springframework.util.Assert;

public class LoginUserInfoHolder {
    private static final ThreadLocal<LoginUserInfo> userInfoHolder = new ThreadLocal<>();
    
    public static void clearLoginUserInfo() {
        userInfoHolder.remove();
    }
    
    public static void setLoginUserInfo(LoginUserInfo loginUserInfo) {
        Assert.notNull(loginUserInfo, "Only non-null UserInfo instances are permitted");
        userInfoHolder.set(loginUserInfo);
    }
    
    public static LoginUserInfo getLoginUserInfo() {
        return userInfoHolder.get();
    }
}
