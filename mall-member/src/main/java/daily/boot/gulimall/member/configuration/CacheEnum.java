package daily.boot.gulimall.member.configuration;

public enum CacheEnum {
    
    /**
     * 用户等级相关信息，默认缓存12小时，单位分钟
     */
    MEMBER_LEVEL_CACHE("member_level", 12 * 60 * 60);
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
