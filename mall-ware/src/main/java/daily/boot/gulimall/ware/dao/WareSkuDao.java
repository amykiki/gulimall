package daily.boot.gulimall.ware.dao;

import daily.boot.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 17:07:54
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
