package daily.boot.gulimall.cart.controller;

import daily.boot.common.Result;
import daily.boot.gulimall.cart.service.CartService;
import daily.boot.gulimall.cart.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiCartController {
    @Autowired
    private CartService cartService;
    
    @GetMapping("/currentUserCartItems/{loginUserId}")
    public Result<List<CartItemVo>> getCurrentCartItems(@PathVariable Long loginUserId) {
        List<CartItemVo> cartItemVoList = cartService.getUserCartItems(loginUserId);
        return Result.ok(cartItemVoList);
    }
}
