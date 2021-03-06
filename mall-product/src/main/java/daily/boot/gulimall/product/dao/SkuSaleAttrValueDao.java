package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import daily.boot.gulimall.product.vo.SkuItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 15:18:58
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {
    
    List<SkuItemVo.SkuItemSaleAttr> listSaleAttrBySpuId(@Param("spuId") Long spuId);
    
    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}
