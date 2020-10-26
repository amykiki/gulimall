package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrGroupDao;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.service.AttrGroupService;

import java.util.List;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    
    @Override
    public PageInfo<AttrGroupEntity> queryPage(PageQueryVo queryVo) {
        IPage<AttrGroupEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public PageInfo<AttrGroupEntity> queryPage(PageQueryVo queryVo, Long categoryId) {
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper = Wrappers.lambdaQuery(AttrGroupEntity.class);
        queryWrapper.eq(null != categoryId && categoryId > 0, AttrGroupEntity::getCatelogId, categoryId);
        String key = queryVo.getKey();
        queryWrapper.and(StringUtils.isNotBlank(key),
                q -> q.eq(AttrGroupEntity::getAttrGroupId, key)
                        .or()
                        .like(AttrGroupEntity::getAttrGroupName, key));
        IPage<AttrGroupEntity> page = this.page(Query.getPage(queryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    @Override
    public List<AttrGroupEntity> getByCatelogId(Long catelogId) {
        return this.list(Wrappers.lambdaQuery(AttrGroupEntity.class)
                                 .eq(AttrGroupEntity::getCatelogId, catelogId));
    }
}