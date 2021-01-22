package daily.boot.gulimall.order.listener;

import daily.boot.gulimall.order.configuration.AlipayTemplate;
import daily.boot.gulimall.order.configuration.CustomDateEditor;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.service.OrderService;
import daily.boot.gulimall.order.vo.AliPayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取支付异步通知结果
 * https://opendocs.alipay.com/open/270/105902
 */
@RestController
@Slf4j
public class OrderPayedListener {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AlipayTemplate alipayTemplate;
    
    @PostMapping("/callback/payed/notify")
    public String handleAlipayNotify(AliPayAsyncVo asyncVo, HttpServletRequest request) throws UnsupportedEncodingException {
        // 获取待验证签名信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            String[] values = parameterMap.get(key);
            String valueStr = String.join(",", values);
            //乱码解决，在出现乱码使使用
            //valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(key, valueStr);
        }
        
    
        //先获取当前订单信息
        OrderEntity orderEntity = orderService.getorderByOrderSn(asyncVo.getOutTradeNo());
        //1. 验签
        boolean verified = alipayTemplate.verifyPay(params, asyncVo.getOutTradeNo(), orderEntity);
        if (!verified) {
            log.warn("支付宝付款签名验证失败，订单号{}, 支付宝流水号{}", asyncVo.getOutTradeNo(), asyncVo.getTradeNo());
            return "error";
        }
        //2. 修改订单状态
        return orderService.handlePayResult(asyncVo);
    }
}
