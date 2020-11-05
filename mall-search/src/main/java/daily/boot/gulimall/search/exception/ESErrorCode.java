package daily.boot.gulimall.search.exception;

import daily.boot.common.exception.error.ErrorCode;

public enum ESErrorCode implements ErrorCode {
    ES_INDEX_ERROR("ES_001", "ES索引操作失败"),
    ES_ENTITY_ERROR("ES_002", "ES实体类注解未指定"),
    ES_MAPPING_ERROR("ES_003", "ES映射MAPPING失败"),
    ES_DOC_ERROR("ES_004", "ES-DOC操作失败"),
    ES_UPDATE_ERROR("ES_005", "ES-DELETE操作失败"),
    ES_DELETE_ERROR("ES_006", "ES-UPDATE操作失败"),
    ES_BULK_ERROR("ES_007", "ES-BATCH操作失败"),
    ES_SEARCH_ERROR("ES_008", "ES-SEARCH操作失败");
    
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
    
    ESErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ESErrorCode{" +
               "code='" + code + '\'' +
               ", message='" + message + '\'' +
               "} " + super.toString();
    }
}
