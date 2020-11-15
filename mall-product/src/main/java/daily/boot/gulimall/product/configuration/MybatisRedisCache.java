package daily.boot.gulimall.product.configuration;

import daily.boot.gulimall.product.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class MybatisRedisCache implements Cache {
    // redis过期时间
    private static final long EXPIRE_TIME_IN_MINUTES = 30;
    
    //读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    
    //id为mapper的namespace，id来为每个namespace对应一个redis的hash结构，在clear时只删掉本namespace的缓存即可
    private String id;
    
    //使用redisTemplate来读写
    private RedisTemplate<Object, Object> redisTemplate;
    
    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void putObject(Object key, Object value) {
        try {
            checkRedisTemplate();
            redisTemplate.opsForHash().put(id, key.toString(), value);
            Long idTtl = (Long) redisTemplate.execute((RedisCallback<Object>) connection -> {
                Long ttl = connection.ttl(id.getBytes());
                return ttl;
            });
            if (idTtl == -1) {
                redisTemplate.expire(id, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
            }
            log.debug("Mybatis-Cache({})====>putObject({})====> SUCCESS", id, key);
        } catch (Exception e) {
            log.error("Mybatis-Cache({})====>putObject({})====> failed", id, key , e);
        }
        
    }
    
    @Override
    public Object getObject(Object key) {
        try {
            checkRedisTemplate();
            if (key != null) {
                Object val = redisTemplate.opsForHash().get(id, key.toString());
                if (val != null) {
                    log.debug("Mybatis-Cache({})====>getObject({})====> SUCCESS", id, key);
                }
                return val;
            }
        } catch (Exception e) {
            log.error("Mybatis-Cache({})====>getObject({})====> failed", id, key, e);
        }
        return null;
    }
    
    @Override
    public Object removeObject(Object key) {
        try {
            checkRedisTemplate();
            if (key != null) {
                redisTemplate.opsForHash().delete(id, key.toString());
                log.debug("Mybatis-Cache({})====>removeObject({})====> SUCCESS", id, key);
            }
        } catch (Exception e) {
            log.error("Mybatis-Cache({})====>removeObject({})====> failed", id, key, e);
        }
        return null;
    }
    
    @Override
    public void clear() {
        try {
            checkRedisTemplate();
            redisTemplate.delete(id);
            log.debug("Mybatis-Cache({})====>clear====> SUCCESS", id);
        } catch (Exception e) {
            log.error("Mybatis-Cache({})====>clear====> failed", id, e);
        }
    }
    
    @Override
    public int getSize() {
        try {
            checkRedisTemplate();
            Long size = redisTemplate.opsForHash().size(id);
            log.debug("Mybatis-Cache====>getSize({}:{})====> SUCCESS", id, size);
            return size.intValue();
        } catch (Exception e) {
            log.error("Mybatis-Cache====>getSize({})====> failed", id, e);
        }
        return -1;
    }
    
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
    
    private void checkRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        }
    }
}
