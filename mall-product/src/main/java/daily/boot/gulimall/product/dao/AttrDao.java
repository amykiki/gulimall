package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.configuration.MybatisRedisCache;
import daily.boot.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 15:18:58
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface AttrDao extends BaseMapper<AttrEntity> {
    
    List<Long> selectableIds(@Param("attrIds") List<Long> attrIds);
}
