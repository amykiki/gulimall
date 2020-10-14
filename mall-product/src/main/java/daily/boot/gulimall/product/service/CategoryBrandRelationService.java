package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.CategoryBrandRelationEntity;


/**
 * 品牌分类关联
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<CategoryBrandRelationEntity> queryPage(PageQueryVo queryVo);
}

