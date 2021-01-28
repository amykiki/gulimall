package daily.boot.gulimall.seckill.webController;

import daily.boot.gulimall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SeckillWebController {
    @Autowired
    private SeckillService seckillService;
    
    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId,
            @RequestParam("key") String key, @RequestParam("num") Integer num, Model model) {
        //抢购成功，返回订单序列号
        try {
            String orderSn = seckillService.kill(killId, key, num);
            model.addAttribute("orderSn", orderSn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
        
    }
}
