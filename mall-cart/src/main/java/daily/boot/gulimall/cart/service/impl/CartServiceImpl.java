package daily.boot.gulimall.cart.service.impl;

import daily.boot.gulimall.cart.security.CartUserKeyHolder;
import daily.boot.gulimall.cart.service.CartService;
import daily.boot.gulimall.cart.service.RemoteService;
import daily.boot.gulimall.cart.to.CartUserKey;
import daily.boot.gulimall.cart.vo.CartItemVo;
import daily.boot.gulimall.cart.vo.CartVo;
import daily.boot.gulimall.service.api.to.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Service
public class CartServiceImpl implements CartService {
    private static final String CART_PREFIX = "gulimall:cart:";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private RemoteService remoteService;
    
    @Autowired
    private ExecutorService cartExecutor;
    
    @Override
    public List<CartItemVo> getUserCartItems() {
        return null;
    }
    
    @Override
    public void addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //获取的购物车redis操作对象
        BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
        
        //判断redis中是否有该商品信息
        CartItemVo cartItemVo = cartOps.get(skuId.toString());
        if (cartItemVo == null) {
            //添加新的商品到购物车redis
            //异步获取商品信息
            CompletableFuture<SkuInfoVo> skuInfoVoFuture = CompletableFuture.supplyAsync(() -> remoteService.getSkuInfo(skuId), cartExecutor);
            //异步获取商品属性信息
            CompletableFuture<List<String>> skuAttrsFuture = CompletableFuture.supplyAsync(() -> remoteService.getSkuSaleAttrValues(skuId), cartExecutor);
    
            SkuInfoVo skuInfoVo = skuInfoVoFuture.get();
            List<String> skuAttrs = skuAttrsFuture.get();
    
            cartItemVo = new CartItemVo();
            cartItemVo.setSkuId(skuId);
            cartItemVo.setTitle(skuInfoVo.getSkuTitle());
            cartItemVo.setImage(skuInfoVo.getSkuDefaultImg());
            cartItemVo.setPrice(skuInfoVo.getPrice());
            cartItemVo.setSkuAttrValues(skuAttrs);
            cartItemVo.setCount(num);
        }else {
            //购物车有商品，修改数量
            cartItemVo.setCount(cartItemVo.getCount() +num);
        }
        
        //修改redis数据
        cartOps.put(skuId.toString(), cartItemVo);
    }
    
    @Override
    public CartItemVo getCartItem(Long skuId) {
    
        BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
        return cartOps.get(skuId.toString());
    }
    
    /**
     * 获取用户登录或者未登录购物车里所有的数据
     * @return
     */
    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        CartUserKey cartUserKey = CartUserKeyHolder.getCartUserKey();
        CartVo cartVo = new CartVo();
        if (cartUserKey.getLoginUserInfo() != null) {
            //用户已登录
            //获取临时购物车的数据进行合并
            String tempCartKey = CART_PREFIX + cartUserKey.getUserKey();
            List<CartItemVo> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要合并
                for (CartItemVo cartItemVo : tempCartItems) {
                    addToCart(cartItemVo.getSkuId(), cartItemVo.getCount());
                }
                //清除临时购物车数据
                clearCartInfo(tempCartKey);
            }
            
            //获取登录后购物车数据，包含合并过来的临时购物车数据和登录后购物车数据
            String cartKey = CART_PREFIX + cartUserKey.getLoginUserInfo().getUserId();
            List<CartItemVo> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);
        } else {
            //没登录
            List<CartItemVo> cartItems = getCartItems(CART_PREFIX + cartUserKey.getUserKey());
            cartVo.setItems(cartItems);
        }
        return cartVo;
    }
    
    
    
    @Override
    public void checkItem(Long skuId, Integer checked) {
        //查询购物车里商品
        CartItemVo cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(checked == 1);
        
        //存入redis
        BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
        cartOps.put(skuId.toString(), cartItem);
    }
    
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里商品
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCount(num);
    
        //存入redis
        BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
        cartOps.put(skuId.toString(), cartItem);
    }
    
    @Override
    public void deleteCartInfo(Long skuId) {
        BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }
    
    public void clearCartInfo(String cartKey) {
        redisTemplate.delete(cartKey);
    }
    
    /**
     * 获取要操作的购物车
     */
    private BoundHashOperations<String, String, CartItemVo> getCartOps() {
        //获取当前购物车用户信息
        CartUserKey cartUserKey = CartUserKeyHolder.getCartUserKey();
        String cartKey = "";
        if (cartUserKey.getLoginUserInfo() != null) {
            cartKey = CART_PREFIX + cartUserKey.getLoginUserInfo().getUserId();
        } else {
            cartKey = CART_PREFIX + cartUserKey.getUserKey();
        }
        
        //绑定指定的key操作Redis
        BoundHashOperations<String, String, CartItemVo> hashOperations = redisTemplate.boundHashOps(cartKey);
        return hashOperations;
    }
    
    private List<CartItemVo> getCartItems(String cartKey) {
        //获取购物车里所有商品
        BoundHashOperations<String, String, CartItemVo> cartOps = redisTemplate.boundHashOps(cartKey);
        return cartOps.values();
    }
    
}
