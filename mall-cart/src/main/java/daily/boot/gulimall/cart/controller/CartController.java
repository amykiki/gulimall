package daily.boot.gulimall.cart.controller;

import daily.boot.gulimall.cart.service.CartService;
import daily.boot.gulimall.cart.vo.CartItemVo;
import daily.boot.gulimall.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
//@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }
    
    /**
     * 获取当前用户的购物车商品项
     *
     * @return
     */
    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    public List<CartItemVo> getCurrentCartItems() {
        return cartService.getUserCartItems();
    }
    
    /**
     * 去购物车页面的请求
     * 浏览器使用cookie:user-key标识用户身份，一个月后过期
     * 如果第一次使用购物车功能，会给一个临时的用户身份
     * 浏览器以后访问都会带上这个cookie
     * <p>
     * 已登录: session中有用户身份
     * 没登录: 安装cookie里user-key来做，第一次如果没有临时用户，自动创建一个临时用户
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart", cartVo);
        return "cartList";
    }
    
    /**
     * 添加商品到购物车
     * 使用重定向技术，那么最后展示给用户的页面只具备显示添加的购物车项目功能，不具备添加商品功能，
     * 这样即使用户重复刷新页面，也不会出现重复添加功能
     * <p>
     * redirectAttributes.addFlashAttribute(): 将数据放在session中，可以在页面取出，但是只能取一次
     * redirectAttributes.addAttribute(): 将数据放在url中
     *
     * @param skuId
     * @param num
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId,
            @RequestParam("num") Integer num,
            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:/addToCartSuccessPage.html";
    }
    
    /**
     * 显示添加到购物车成功页面
     *
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
            Model model) {
        //查询购物车数据
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);
        return "success";
    }
    
    /**
     * 商品是否被选中
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
            @RequestParam("checked") Integer checked) {
        cartService.checkItem(skuId, checked);
        return "redirect:/cart.html";
    }
    
    /**
     * 改变商品数量
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,
            @RequestParam("num") Integer num) {
        cartService.changeItemCount(skuId, num);
        return "redirect:/cart.html";
    }
    
    /**
     * 删除购物车商品信息
     * @param skuId
     * @return
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteCartInfo(skuId);
        return "redirect:/cart.html";
    }
    
}
