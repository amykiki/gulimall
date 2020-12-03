package daily.boot.gulimall.member.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    USERNAME_EXIST("MEM-001", "用户名已存在"),
    PHONE_EXIST("MEM-002", "用户手机号已存在"),
    USERNAME_NOT_EXIST("MEM-003", "用户名不存在"),
    AUTO_LOGIN_UPDATE_FAIL("MEM-004", "更新自动登录标识失败");
    
    private String code;
    private String message;
    
    MemberErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
