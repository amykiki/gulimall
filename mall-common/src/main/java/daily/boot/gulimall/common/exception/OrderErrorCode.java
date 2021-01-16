package daily.boot.gulimall.common.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum OrderErrorCode implements ErrorCode {
    ORDER_LOCK_STOCK_FAIL("ORDER_001", "下订单锁库存失败");
    
    OrderErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    private String code;
    private String message;
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
