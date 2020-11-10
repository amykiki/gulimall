package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<CategoryEntity> queryPage(PageQueryVo queryVo);
    
    /**
     * 获取所有品牌分类以及子分类，以树形结构组装起来
     * @return
     */
    List<CategoryEntity> listWithTree();
    
    /**
     * 检查品牌没有被其他业务引用才能删除
     * @param delIds
     */
    void removeMenuByIds(List<Long> delIds);
    
    Long[] findCatelogPath(Long catelogId);
    
    CategoryEntity getSimpleCategoryEntityById(Long catId);
    
    void updateCascaded(CategoryEntity category);
    
    //查出所有一级分类
    List<CategoryEntity> getLevel1Categorys();
    
    Map<String, List<Catelog2Vo>> getCatalogJson();
}

