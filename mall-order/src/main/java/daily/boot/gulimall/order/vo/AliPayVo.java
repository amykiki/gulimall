package daily.boot.gulimall.order.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AliPayVo {
    /**
     * 商户订单号，必填
     */
    @JSONField(name="out_trade_no")
    private String outTradeNo;
    /**
     * 付款金额，必填
     */
    @JSONField(name = "total_amount")
    private String totalAmount;
    
    @JSONField(name = "product_code")
    private String productCode = "FAST_INSTANT_TRADE_PAY";
    
    @JSONField(name="timeout_express")
    /**
     * 该参数在请求到支付宝时开始计时,该笔订单允许的最晚付款时间，逾期将关闭交易。
     * 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天
     * （1c-当天的情况下，无论交易何时创建，都在0点关闭）。
     * 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     */
    private String timeoutExpress;
    /**
     * 订单名称，必填
     */
    private String subject;
    /**
     * 商品描述，可为空
     */
    private String body;
    
}
