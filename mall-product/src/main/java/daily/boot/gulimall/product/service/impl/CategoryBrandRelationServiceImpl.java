package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.CategoryBrandRelationDao;
import daily.boot.gulimall.product.entity.CategoryBrandRelationEntity;
import daily.boot.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageInfo<CategoryBrandRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<CategoryBrandRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}