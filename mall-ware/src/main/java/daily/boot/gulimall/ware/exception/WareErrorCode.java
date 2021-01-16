package daily.boot.gulimall.ware.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum WareErrorCode implements ErrorCode {
    WARE_PURCHASE_STATUS_ERROR("WARE_001", "采购单已不能合并采购需求"),
    WARE_PURCHASE_NOT_EXIST_ERROR("WARE_002", "采购单不存在"),
    WARE_PURCHASE_DETAIL_CANNOT_MERGED("WARE_003", "采购需求正在完成中或已完成，不能再合并"),
    WARE_PURCHASE_DETAIL_NOT_EXIST("WARE_004", "采购需求不存在"),
    WARE_PURCHASE_DONE_EMPTY("WARE_005", "采购单完成详情为空"),
    WARE_PURCHASE_NOT_VALID("WARE_006", "采购单号无效"),
    WARE_SKU_NO_STOCK("WARE_007", "SKU无货");
    
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
    
    WareErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "WareErrorCode{" +
               "code='" + code + '\'' +
               ", message='" + message + '\'' +
               "} " + super.toString();
    }
}
