package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.service.api.to.OrderItemEntityTo;
import daily.boot.gulimall.service.api.to.OrderTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${gulimall.feign.order}")
public interface OrderFeignService {
    
    @GetMapping("/api/order/order/{memberId}")
    Result<List<OrderTo>> getOrdersByMemberId(@PathVariable("memberId") Long memberId);
    
    @GetMapping("/api/order/order/status/{orderSn}")
    Result<OrderTo> getOrderStatus(@PathVariable("orderSn") String orderSn);
    
    @PostMapping("/api/order/order/listWithItem")
    Result<PageInfo<OrderTo>> listWithItem(@RequestBody PageQueryVo pageQueryVo);
}
