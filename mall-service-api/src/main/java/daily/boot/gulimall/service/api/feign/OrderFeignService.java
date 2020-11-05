package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.OrderTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${gulimall.feign.order}")
public interface OrderFeignService {
    
    @GetMapping("/order/order/{memberId}")
    Result<List<OrderTo>> getOrdersByMemberId(@PathVariable("memberId") Long memberId);
}
