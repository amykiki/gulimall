package daily.boot.gulimall.product.dao;

import daily.boot.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-13 14:20:31
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
