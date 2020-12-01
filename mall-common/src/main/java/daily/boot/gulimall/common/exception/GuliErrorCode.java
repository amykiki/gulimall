package daily.boot.gulimall.common.exception;

import daily.boot.common.exception.error.ErrorCode;

/**
 * <p>Title: BizCodeEnum</p>
 * Description：
 * 错误码和错误信息定义类
 * 1. 错误码定义规则为5为数字
 * 2. 前两位表示业务场景，最后三位表示错误码。
 *    例如：10000。10:通用 000:成功
 *         10999 10:通用 999:未知异常
 * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 * 错误码列表：
 * 10: 通用
 * 001：参数格式校验
 * 11: 商品
 * 12: 订单
 * 13: 购物车
 * 14: 物流
 */
public enum GuliErrorCode implements ErrorCode {
    SMS_CODE_EXCEPTION("SMS_001", "验证码获取频率调高，请稍后再试"),
    SMS_SEND_EXCEPTION("SMS-002", "短信发送异常");
    private String code;
    private String message;
    
    GuliErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String  getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
