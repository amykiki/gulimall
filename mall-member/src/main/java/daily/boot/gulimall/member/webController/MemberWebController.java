package daily.boot.gulimall.member.webController;

import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.member.security.LoginUserInfo;
import daily.boot.gulimall.member.security.LoginUserInfoHolder;
import daily.boot.gulimall.member.service.RemoteService;
import daily.boot.gulimall.service.api.to.OrderTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberWebController {
    @Autowired
    private RemoteService remoteService;
    
    //http://member.gulimall.com/memberOrder.html?charset=utf-8&out_trade_no=202101222347382101352644096681836545&method=alipay.trade.page.pay.return&total_amount=12099.00&sign=owk9ha2xDEqF%2FZCGC%2FfusfMxSRjVKLLMp3WWvlrac7VJcN15KNZdHJzcaMlrtC2mN%2FM2c3umJ1pK07HJYVhtrWXQ7cgQEHlp6S%2F2mIviQ5FzD%2BrkyJMUczT0BKP15KG%2BJZTy%2FLl3aC3QcQ8n%2Fet%2BX%2BtyGxUOlQE89V1CZ8lS9XxSa%2F1yVLk8DPuBum3AedwNUohGPHVpp4zxmOBIiLCLyOTY5dTF0%2F0ZUv3WJO8QSQaIGhOwazMplwTwZ14wmYB06kS9F8s2%2BH5xJmr1EH9kFL7goTJVXtl1%2BG8iG1mgj5KXTjE27p7Ub0yUZQC2jmOxPVkFeoFzT8tttnZenV9S2Q%3D%3D&trade_no=2021012222001463810501289292&auth_app_id=2021000117602053&version=1.0&app_id=2021000117602053&sign_type=RSA2&seller_id=2088621955178365&timestamp=2021-01-22+23%3A49%3A22
    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum, Model model) {
        
        //查出当前登录用户的所有订单列表数据
        LoginUserInfo loginUserInfo = LoginUserInfoHolder.getLoginUserInfo();
        PageQueryVo pageQueryVo = new PageQueryVo();
        pageQueryVo.setKey(String.valueOf(loginUserInfo.getUserId()));
        pageQueryVo.setPage(pageNum);
        pageQueryVo.setLimit(4L);
        PageInfo<OrderTo> orders = remoteService.listWithItem(pageQueryVo);
        model.addAttribute("orders", orders);
        return "orderList";
    }
}
