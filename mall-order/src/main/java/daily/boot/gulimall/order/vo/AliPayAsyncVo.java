package daily.boot.gulimall.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 参考 spring-boot 下划线和驼峰转换 https://adolphor.com/2019/11/16/spring-boot-under-lower-camel.html
 * Spring Boot Jackson命名策略 https://blog.csdn.net/wo541075754/article/details/103799970
 * 这里遇到了一个坑，支付宝回调参数的形式是下划线分割的
 * 我以为配置了JsonProperty，就能在前端自动把下划线转为驼峰，实际不行
 * 参考 https://adolphor.com/2019/11/16/spring-boot-under-lower-camel.html
 * 在本例中需要做特殊处理
 */
@Data
@ToString
public class AliPayAsyncVo {
    /**
     * 交易创建时间。该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss。
     *
     * 示例值：2015-04-27 15:45:57
     */
    @JsonProperty("gmt_create")
    private String gmtCreate;
    /**
     * 交易付款时间。该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss。
     *
     * 示例值：2015-04-27 15:45:57
     */
    @JsonProperty("gmt_payment")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtPayment;
    /**
     * 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。
     *
     * 示例值：2015-14-27 15:45:58
     */
    @JsonProperty("notify_time")
    private Date notifyTime;
    /**
     * 签名。
     */
    private String sign;
    /**
     * 签名类型。签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    @JsonProperty("sign_type")
    private String signType;
    /**
     * 买家支付宝用户号。买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。
     *
     * 示例值：2088102122524333
     */
    @JsonProperty("buyer_id")
    private String buyerId;
    /**
     * 开票金额。用户在交易中支付的可开发票的金额，单位为元，精确到小数点后 2 位。
     *
     * 示例值：10.00
     */
    @JsonProperty("invoice_amount")
    private String invoiceAmount;
    /**
     * 通知校验 ID。
     *
     * 示例值：ac05099524730693a8b330c5ecf72da978
     */
    @JsonProperty("notify_id")
    private String notifyId;
    /**
     * 通知类型。
     *
     * 示例值：trade_status_sync
     */
    @JsonProperty("notify_type")
    private String notifyType;
    /**
     * 支付金额信息。支付成功的各个渠道金额信息。详情请参见 资金明细信息说明。
     *
     * 示例值：[{“amount”:“15.00”,“fundChannel”:“ALIPAYACCOUNT”}]
     */
    @JsonProperty("fund_bill_list")
    private String fundBillList;
    /**
     * 商户订单号。原支付请求的商户订单号。
     *
     * 示例值：6823789339978248
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 订单金额。本次交易支付的订单金额，单位为人民币（元），精确到小数点后 2 位。
     *
     * 示例值：20.00
     */
    @JsonProperty("total_amount")
    private String totalAmount;
    /**
     * 实收金额。商家在交易中实际收到的款项，单位为元，精确到小数点后 2 位。
     *
     * 示例值：15.00
     */
    @JsonProperty("receipt_amount")
    private String receiptAmount;
    /**
     * 集分宝金额。使用集分宝支付的金额，单位为元，精确到小数点后 2 位。
     *
     * 示例值：12.00
     */
    @JsonProperty("point_amount")
    private String pointAmount;
    /**
     * 用户在交易中支付的金额，单位为元，精确到小数点付款金额。后 2 位。
     *
     * 示例值：13.88
     */
    @JsonProperty("buyer_pay_amount")
    private String buyerPayAmount;
    /**
     * 交易状态。交易目前所处的状态。详情请参见 交易状态说明。
     * 枚举名称	枚举说明
     * WAIT_BUYER_PAY	交易创建，等待买家付款。
     * TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款。
     * TRADE_SUCCESS	交易支付成功。
     * TRADE_FINISHED	交易结束，不可退款。
     *
     * 示例值：TRADE_CLOSED
     */
    @JsonProperty("trade_status")
    private String tradeStatus;
    /**
     * 支付宝交易号。支付宝交易凭证号。
     *
     * 示例值：2013112011001004330000121536
     */
    @JsonProperty("trade_no")
    private String tradeNo;
    /**
     * 开发者的 app_id。支付宝分配给开发者的应用 ID。
     *
     * 示例值：2014072300007148
     */
    @JsonProperty("app_id")
    private String appId;
    /**
     * 卖家支付宝用户号。
     *
     * 示例值：2088101106499364
     */
    @JsonProperty("seller_id")
    private String sellerId;
    /**
     * 授权方的 appid。由于本接口暂不开放第三方应用授权，因此 auth_app_id=app_id。
     *
     * 示例值：2014072300007148
     */
    @JsonProperty("auth_app_id")
    private String authAppId;
    /**
     * 编码格式。如 utf-8、gbk、gb2312 等。
     *
     * 示例值：utf-8
     */
    private String charset;
    /**
     * 订单标题。商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。
     */
    private String subject;
    /**
     * 商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。
     */
    private String body;
    /**
     * 调用的接口版本。固定为：1.0。
     *
     * 示例值：1.0
     */
    private String version;
}
