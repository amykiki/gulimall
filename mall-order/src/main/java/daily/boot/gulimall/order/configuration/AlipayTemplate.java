package daily.boot.gulimall.order.configuration;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.enums.OrderStatusEnum;
import daily.boot.gulimall.order.service.OrderService;
import daily.boot.gulimall.order.vo.AliPayVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@ConfigurationProperties(prefix = "gulimall.order.pay")
@Component
@Data
@Slf4j
public class AlipayTemplate{
    /**
     * 参数说明参考 https://opendocs.alipay.com/open/270/105902
     */
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    private String appId;
    // 商户私钥，您的PKCS8格式RSA2私钥
    private String alipayPrivateKey;
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipayPublicKey;
    
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notifyUrl;
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String returnUrl;
    private String signType = "RSA2";
    private String charset = "utf-8";
    private String gatewayUrl;
    private String timeout;
    
    public String pay(AliPayVo vo) throws AlipayApiException {
        //设置关单超时
        vo.setTimeoutExpress(timeout);
        
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, alipayPrivateKey, "json", charset, alipayPublicKey, signType);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        
        //商户订单号，商户网站系统中唯一订单号，必填
        String bizContent = JSON.toJSONString(vo);
        alipayRequest.setBizContent(bizContent);
    
        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
        
    }
    
    /**
     * 使用 RSA 的验签方法，通过签名字符串、签名参数（经过 base64 解码）及支付宝公钥验证签名。
     *
     * 需要严格按照如下描述校验通知数据的正确性：
     *
     * 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
     *
     * 判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）；
     *
     * 校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商户可能有多个 seller_id/seller_email）；
     *
     * 验证 app_id 是否为该商户本身。
     *
     * 上述 1、2、3、4 有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功。
     * @param params
     * @param outTradeNo
     * @return
     */
    public boolean verifyPay(Map<String, String> params, String outTradeNo, OrderEntity orderEntity) {
        //1. 验证签名
        boolean result;
        try {
           result = AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset, signType);
        } catch (AlipayApiException e) {
            log.error("阿里支付签名校验异常", e);
            return false;
        }
        if (!result) {
            log.info("支付订单{}阿里支付签名校验错误", outTradeNo);
            return false;
        }
        
        //2. 验证数据
        // 2.1 确认订单信息可以找到
        if (orderEntity == null) {
            log.info("订单{}找不到信息", outTradeNo);
            return false;
        }
        // 2.2 确认订单状态为未支付
        if (!orderEntity.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            log.info("订单{}状态为{}，不能支付", outTradeNo, orderEntity.getStatus());
            return false;
        }
        // 2.3 确认订单金额判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）；
        BigDecimal paiedAmount = orderEntity.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        String totalAmountStr = params.get("total_amount");
        BigDecimal totalAmount = new BigDecimal(totalAmountStr);
        totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_UP);
        if (paiedAmount.compareTo(totalAmount) != 0) {
            log.info("订单{}应付金额{}和支付宝支付金额{}不一致", outTradeNo, paiedAmount.toString(), totalAmount.toString());
            return false;
        }
        // 2.4 校验seller_id
        String sellerId = params.get("seller_id");
        log.info("sellerId is {}", sellerId);
        
        // 2.5 校验appId为商户本身id
        String orderAppId = params.get("app_id");
        if (!appId.equals(orderAppId)) {
            log.info("订单{}appId{}与应用appId不一致", outTradeNo, orderAppId);
            return false;
        }
        return true;
    }
}
