package daily.boot.gulimall.authserver.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    AUTH_REG_EXCEPTION("AUTH_001", "注册未知异常"),
    SAVE_AUTO_TOKEN_FAIL("AUTH_002", "保存自动登录token失败"),
    UPDATE_AUTO_TOKEN_FAIL("AUTH_003", "更新自动登录token失败");
    
    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    String code;
    String message;
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
