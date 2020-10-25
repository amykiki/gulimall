package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import daily.boot.gulimall.product.entity.BrandEntity;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.BrandService;
import daily.boot.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CategoryBrandRelationDao;
import daily.boot.gulimall.product.entity.CategoryBrandRelationEntity;
import daily.boot.gulimall.product.service.CategoryBrandRelationService;

import java.util.List;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    
    @Override
    public PageInfo<CategoryBrandRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<CategoryBrandRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<CategoryBrandRelationEntity> getBrandCategories(Long brandId) {
        List<CategoryBrandRelationEntity> list =
                this.list(Wrappers.lambdaQuery(CategoryBrandRelationEntity.class)
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
        return list;
    }
    
    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<BrandEntity> brandEntities = this.baseMapper.getBrandsByCatId(catId);
        return brandEntities;
    }
    
    @Override
    public void details(CategoryBrandRelationEntity categoryBrandRelation) {
        //1. 获取brandName
        BrandEntity brand = brandService.getSimpleBrandEntityById(categoryBrandRelation.getBrandId());
        CategoryEntity category = categoryService.getSimpleCategoryEntityById(categoryBrandRelation.getCatelogId());
    
        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());
    
        this.save(categoryBrandRelation);
    }
    
    @Override
    public void updateRelationBrand(Long brandId, String brandName) {
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(brandId);
        entity.setBrandName(brandName);
        LambdaUpdateWrapper<CategoryBrandRelationEntity> updateWrapper =
                Wrappers.lambdaUpdate(CategoryBrandRelationEntity.class)
                .eq(CategoryBrandRelationEntity::getBrandId, brandId);
        this.update(entity, updateWrapper);
    }
    
    @Override
    public void updateRelationCategory(Long categoryId, String categoryName) {
        this.update(Wrappers.lambdaUpdate(CategoryBrandRelationEntity.class)
                .set(CategoryBrandRelationEntity::getCatelogId, categoryId)
                .set(CategoryBrandRelationEntity::getCatelogName, categoryName)
                .eq(CategoryBrandRelationEntity::getCatelogId, categoryId));
    
    }
}