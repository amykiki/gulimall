package daily.boot.gulimall.ware.dao;

import daily.boot.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 17:07:54
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    
    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
    
    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
