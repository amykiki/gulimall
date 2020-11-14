package daily.boot.gulimall.product.configuration;

public enum CacheEnum {
    
    /**
     * 用户jwt token 缓存空间 ttl 1天
     */
    CATEGORY_CACHE("category", 24 * 60 * 60),
    /**
     * 验证码缓存 5分钟ttl
     */
    SMS_CAPTCHA_CACHE("smsCode", 5 * 60),
    /**
     * 商品SKU信息缓存，5分钟
     */
    SKU_CACHE("sku", 5 * 60);
    /**
     * 缓存名称
     */
    private final String cacheName;
    /**
     * 缓存过期秒数
     */
    private final int ttlSecond;
    CacheEnum(String cacheName, int ttlSecond) {
        this.cacheName = cacheName;
        this.ttlSecond = ttlSecond;
    }
    
    public String cacheName() {
        return this.cacheName;
    }
    
    
    public int ttlSecond() {
        return this.ttlSecond;
    }

}
