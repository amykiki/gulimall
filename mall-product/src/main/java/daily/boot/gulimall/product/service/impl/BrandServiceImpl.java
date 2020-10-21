package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.BrandDao;
import daily.boot.gulimall.product.entity.BrandEntity;
import daily.boot.gulimall.product.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    
    @Override
    public PageInfo<BrandEntity> queryPage(PageQueryVo queryVo, BrandEntity brandEntity) {
        
        LambdaQueryWrapper<BrandEntity> queryWrapper = Wrappers.<BrandEntity>lambdaQuery()
                .like(StringUtils.isNotBlank(brandEntity.getName()), BrandEntity::getName, brandEntity.getName())
                .eq(StringUtils.isNotBlank(brandEntity.getFirstLetter()), BrandEntity::getFirstLetter, brandEntity.getFirstLetter());
        IPage<BrandEntity> page = this.page(
                Query.getPage(queryVo),
                queryWrapper
        );
        return PageInfo.of(page);
    }
    
    @Override
    public PageInfo<BrandEntity> queryPage(PageQueryVo queryVo) {
        LambdaQueryWrapper<BrandEntity> query = Wrappers.<BrandEntity>lambdaQuery();
        if (StringUtils.isNotBlank(queryVo.getKey())) {
            query = Wrappers.<BrandEntity>lambdaQuery()
                    .eq(BrandEntity::getBrandId, queryVo.getKey())
                    .or()
                    .like(BrandEntity::getName, queryVo.getKey());
        }
        IPage<BrandEntity> page = this.page(Query.getPage(queryVo), query);
        return PageInfo.of(page);
    }
}