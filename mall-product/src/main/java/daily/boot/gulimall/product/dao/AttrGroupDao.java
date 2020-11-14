package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import daily.boot.gulimall.product.vo.SkuItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 15:18:58
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {
    
    List<SkuItemVo.SpuItemAttrGroup> listAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId);
}
