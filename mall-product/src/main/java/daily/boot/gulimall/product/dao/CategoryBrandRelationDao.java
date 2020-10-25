package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.entity.BrandEntity;
import daily.boot.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 品牌分类关联
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 15:18:58
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {
    List<BrandEntity> getBrandsByCatId(Long catId);
}
