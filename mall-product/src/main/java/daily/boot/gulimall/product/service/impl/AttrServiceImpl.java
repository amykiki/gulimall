package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.constant.ProductConstant;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrDao;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;
import daily.boot.gulimall.product.service.AttrGroupService;
import daily.boot.gulimall.product.service.AttrService;
import daily.boot.gulimall.product.service.CategoryService;
import daily.boot.gulimall.product.vo.AttrRespVo;
import daily.boot.gulimall.product.vo.AttrVo;
import daily.boot.unified.dispose.exception.BusinessException;
import daily.boot.unified.dispose.exception.error.CommonErrorCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrGroupService attrGroupService;
    
    @Override
    public PageInfo<AttrEntity> queryPage(PageQueryVo queryVo) {
        IPage<AttrEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    @Transactional
    public void saveCascaded(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        //1. 保存Entity，可以得到回写的attrId
        this.save(attrEntity);
        //2. 保存和AttrGroup的关联关系，仅只保存基础属性的关联关系
        if (attrVo.getAttrGroupId() != null && attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationService.save(relationEntity);
        }
    }
    
    @Override
    public PageInfo<AttrRespVo> queryPage(PageQueryVo pageQueryVo, Long catelogId, String attrType) {
        boolean catelogIdValid = false;
        if (catelogId != null && catelogId > 0) {
            catelogIdValid = true;
        }
        LambdaQueryWrapper<AttrEntity> queryWrapper = Wrappers.lambdaQuery(AttrEntity.class);
        queryWrapper.eq(AttrEntity::getAttrType, getAttrType(attrType).getCode());
        queryWrapper.eq(catelogIdValid, AttrEntity::getCatelogId, catelogId);
        //条件查询query
        addPageQuery(queryWrapper, pageQueryVo.getKey());
        //分页查询
        IPage<AttrEntity> entityPage = this.page(Query.getPage(pageQueryVo), queryWrapper);
        
        List<AttrEntity> attrEntities = entityPage.getRecords();
        String queryCatelogName = null;
        if (catelogIdValid) {
            queryCatelogName = getCatelogName(catelogId);
        }
        boolean finalCatelogIdValid = catelogIdValid;
        String finalQueryCatelogName = queryCatelogName;
        List<AttrRespVo> respVoList = attrEntities.stream().map(entity -> {
            AttrRespVo respVo = new AttrRespVo();
            if (getAttrType(attrType) == ProductConstant.AttrEnum.ATTR_TYPE_BASE) {
                String attrGroupName = getAttrGroupName(entity.getAttrId());
                respVo.setGroupName(attrGroupName);
            }
            String catelogName = finalCatelogIdValid ? finalQueryCatelogName : getCatelogName(entity.getCatelogId());
            
            BeanUtils.copyProperties(entity, respVo);
            respVo.setCatelogName(catelogName);
           
            return respVo;
        }).collect(Collectors.toList());
        return PageInfo.of(respVoList, entityPage);
    }
    
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        // 1. 获取基本属性
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        
        //2. 获取关联属性
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getByAttrId(attrId);
            if (relationEntity != null) {
                attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        //3. 获取三级分类id和三级分类路径
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);
        String catelogName = getCatelogName(attrEntity.getCatelogId());
        attrRespVo.setCatelogName(catelogName);
        
        return attrRespVo;
    }
    
    @Override
    public void updateAttrInfo(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);
    
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //更新级联的AttrGroupRelation。
            //如果关联表中没有，则新建，有，则更新
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrVo.getAttrId());
    
            AttrAttrgroupRelationEntity oldRelation = attrAttrgroupRelationService.getByAttrId(attrVo.getAttrId());
            if (oldRelation != null) {
                //有更新
                attrAttrgroupRelationService.updateByAttrId(relationEntity, attrVo.getAttrId());
            }else {
                //没有则新建
                attrAttrgroupRelationService.save(relationEntity);
            }
        }
        
    }
    
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationService.getAllByAttrGroupId(attrGroupId);
        List<Long> attrIds = relationEntities.stream()
                                             .map(AttrAttrgroupRelationEntity::getAttrId)
                                             .collect(Collectors.toList());
        if (attrIds.size() == 0) {
            return null;
        }
        return this.listByIds(attrIds);
    }
    
    @Override
    public PageInfo<AttrEntity> getNoAttrRelation(PageQueryVo pageQueryVo, Long attrGroupId) {
        // 1. 查询attrGroup对应的品类
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
        if (attrGroupEntity == null) {
            throw new BusinessException(CommonErrorCode.PARAM_ERROR, "查询结果为空");
        }
        
        // 2. 查询该品类对应的所有attrGroup
        List<AttrGroupEntity> attrGroupEntities = attrGroupService.getByCatelogId(attrGroupEntity.getCatelogId());
        List<Long> attrGroupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        
        // 3. 查询该品类对应的所有AttrGroup已经绑定的规格参数
        List<AttrAttrgroupRelationEntity> relations = attrAttrgroupRelationService.listByAttrGroupIds(attrGroupIds);
        // 3.2 获取所有已绑定的规格参数
        List<Long> attrIds = relations.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        
        // 4.从所有属性中排除已绑定其不是销售属性的属性
        LambdaQueryWrapper<AttrEntity> queryWrapper = Wrappers.lambdaQuery(AttrEntity.class)
                                                               .eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                                                               .eq(AttrEntity::getCatelogId, attrGroupEntity.getCatelogId())
                                                               .notIn(!CollectionUtils.isEmpty(attrIds), AttrEntity::getAttrId, attrIds);
        addPageQuery(queryWrapper, pageQueryVo.getKey());
    
        IPage<AttrEntity> page = this.page(Query.getPage(pageQueryVo), queryWrapper);
        return PageInfo.of(page);
    }
    
    private String getCatelogName(Long catelogId) {
        CategoryEntity categoryEntity = categoryService.getSimpleCategoryEntityById(catelogId);
        if (categoryEntity != null) {
            return categoryEntity.getName();
        }
        return null;
    }
    
    private String getAttrGroupName(Long attrId) {
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getByAttrId(attrId);
        if (relationEntity != null) {
            AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            return groupEntity.getAttrGroupName();
        }
        return null;
    }
    
    private ProductConstant.AttrEnum getAttrType(String type) {
        return "base".equalsIgnoreCase(type)
                ? ProductConstant.AttrEnum.ATTR_TYPE_BASE
                : ProductConstant.AttrEnum.ATTR_TYPE_SALE;
    }
    
    private void addPageQuery(LambdaQueryWrapper<AttrEntity> queryWrapper,  String key) {
        queryWrapper.and(StringUtils.isNotBlank(key), q ->
                q.eq(AttrEntity::getAttrId, key)
                 .or()
                 .like(AttrEntity::getAttrName, key)
        );
    }
}