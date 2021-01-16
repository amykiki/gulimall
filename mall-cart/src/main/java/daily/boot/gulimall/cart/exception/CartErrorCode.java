package daily.boot.gulimall.cart.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum  CartErrorCode implements ErrorCode {
    CUR_CART_ITEM_NONE("CART_001", "当前用户购物车为空");
    private String code;
    
    private String message;
    
    CartErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String getCode() {
        return null;
    }
    
    @Override
    public String getMessage() {
        return null;
    }
    
    
    @Override
    public String toString() {
        return "CartErrorCode{" +
               "code='" + code + '\'' +
               ", message='" + message + '\'' +
               "} " + super.toString();
    }
}
