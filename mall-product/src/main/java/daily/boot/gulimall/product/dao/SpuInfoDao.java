package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.configuration.MybatisRedisCache;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu信息
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 15:18:58
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

}
