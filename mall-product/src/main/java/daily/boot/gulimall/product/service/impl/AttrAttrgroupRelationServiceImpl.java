package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.constant.ProductConstant;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrAttrgroupRelationDao;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;
import daily.boot.gulimall.product.service.AttrGroupService;
import daily.boot.gulimall.product.service.AttrService;
import daily.boot.gulimall.product.vo.AttrGroupRelationVo;
import daily.boot.unified.dispose.exception.BusinessException;
import daily.boot.unified.dispose.exception.error.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrGroupService attrGroupService;
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
    public List<AttrAttrgroupRelationEntity> listByAttrGroupIds(List<Long> attrGroupIds) {
        if (CollectionUtils.isNotEmpty(attrGroupIds)) {
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                                                                                   .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds);
            return this.list(queryWrapper);
        }
        return null;
    }
    
    @Override
    public List<AttrAttrgroupRelationEntity> listByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isNotEmpty(attrIds)) {
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                                                                                   .in(AttrAttrgroupRelationEntity::getAttrId, attrIds);
            return this.list(queryWrapper);
        }
        return new ArrayList<>();
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
    
    @Override
    public void saveBatchRelations(List<AttrGroupRelationVo> attrGroupRelationVos) {
        //1. 检查attr必须都为base属性且没有绑定过分组属性
        // 单表可考虑使用唯一约束，分库分表情况下唯一约束无法使用
        List<Long> attrIds = attrGroupRelationVos.stream()
                                                 .map(AttrGroupRelationVo::getAttrId)
                                                 .collect(Collectors.toList());
        List<Long> attrGroupIds = attrGroupRelationVos.stream()
                                                      .map(AttrGroupRelationVo::getAttrGroupId)
                                                      .collect(Collectors.toList());
        
        //2. 查询attrIds，应该都没有绑定过分组
        List<AttrAttrgroupRelationEntity> relationEntities = this.listByAttrIds(attrIds);
        if (CollectionUtils.isNotEmpty(relationEntities)) {
            throw new BusinessException(CommonErrorCode.PARAM_ERROR, "规格参数已绑定过分组属性");
        }
        
        //3. 验证attrIds和attrGroupIds合法性，都存在数据库中并且对应的catelogId相同，且规格参数为base参数
        List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
        Map<Long, AttrEntity> attrEntityMap
                = attrEntities.stream()
                              .collect(Collectors.toMap(AttrEntity::getAttrId, Function.identity()));
        
        List<AttrGroupEntity> attrGroupEntities = attrGroupService.listByIds(attrGroupIds);
        Map<Long, AttrGroupEntity> attrGroupEntityMap =
                attrGroupEntities.stream()
                                 .collect(Collectors.toMap(AttrGroupEntity::getAttrGroupId, Function.identity()));
        
        List<AttrAttrgroupRelationEntity> saveRelationEntities = new ArrayList<>();
        for (AttrGroupRelationVo vo : attrGroupRelationVos) {
            AttrEntity attrEntity = attrEntityMap.get(vo.getAttrId());
            AttrGroupEntity groupEntity = attrGroupEntityMap.get(vo.getAttrGroupId());
            if (attrEntity == null
                || groupEntity == null
                || !attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                || !attrEntity.getCatelogId().equals(groupEntity.getCatelogId())) {
                throw new BusinessException(CommonErrorCode.PARAM_ERROR, "规格参数或分组属性无效");
            }
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(vo.getAttrId());
            relationEntity.setAttrGroupId(vo.getAttrGroupId());
            saveRelationEntities.add(relationEntity);
        }
    
        this.saveBatch(saveRelationEntities);
    
    }
}