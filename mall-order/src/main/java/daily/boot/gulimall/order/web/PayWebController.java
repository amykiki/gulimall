package daily.boot.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import daily.boot.gulimall.order.configuration.AlipayTemplate;
import daily.boot.gulimall.order.service.OrderService;
import daily.boot.gulimall.order.vo.AliPayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class PayWebController {
    @Autowired
    private AlipayTemplate alipayTemplate;
    
    @Autowired
    private OrderService orderService;
    
    @ResponseBody
    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        AliPayVo aliPayVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(aliPayVo);
        log.info(pay);
        return pay;
    }
}
