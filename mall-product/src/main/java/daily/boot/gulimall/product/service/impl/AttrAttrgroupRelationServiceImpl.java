package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrAttrgroupRelationDao;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;
import daily.boot.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageInfo<AttrAttrgroupRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public AttrAttrgroupRelationEntity getByAttrId(Long attrId) {
        AttrAttrgroupRelationEntity entity = this.getOne(Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                                                                 .eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
        return entity;
    }
    
    @Override
    public void updateByAttrId(AttrAttrgroupRelationEntity attrAttrgroupRelationEntity, Long attrId) {
        this.update(attrAttrgroupRelationEntity,
                    Wrappers.lambdaUpdate(AttrAttrgroupRelationEntity.class)
                            .eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
    }
    
    @Override
    public List<AttrAttrgroupRelationEntity> getAllByAttrGroupId(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> list = this.list(Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                                                                   .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
        return list;
    }
    
    @Override
    public List<AttrAttrgroupRelationEntity> getAllByAttrGroupIds(List<Long> attrGroupIds) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                                                                               .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds);
        return this.list(queryWrapper);
    }
    
    @Override
    public void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos) {
        List<AttrAttrgroupRelationEntity> relations = Arrays.asList(attrGroupRelationVos).stream().map(vo -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
    
        this.baseMapper.deleteBatchRelations(relations);
    }
}