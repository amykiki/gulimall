package daily.boot.gulimall.service.api.feign;

import daily.boot.common.Result;
import daily.boot.gulimall.service.api.to.CartItemTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gulimall-cart")
public interface CartFeignService {
    
    @GetMapping("/api/currentUserCartItems/{loginUserId}")
    Result<List<CartItemTo>> getCurrentCartItems(@PathVariable("loginUserId") Long loginUserId);
}
