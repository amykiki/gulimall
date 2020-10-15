package daily.boot.gulimall.service.api.feign;

import daily.boot.gulimall.common.utils.Result;
import daily.boot.gulimall.service.api.entity.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${gulimall.feign.order}")
public interface OrderFeignService {
    
    @GetMapping("/order/order/{memberId}")
    Result<List<OrderDto>> getOrdersByMemberId(@PathVariable("memberId") Long memberId);
}
