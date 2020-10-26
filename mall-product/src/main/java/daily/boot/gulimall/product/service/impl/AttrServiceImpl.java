package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import daily.boot.gulimall.product.entity.AttrAttrgroupRelationEntity;
import daily.boot.gulimall.product.entity.AttrGroupEntity;
import daily.boot.gulimall.product.entity.CategoryEntity;
import daily.boot.gulimall.product.service.AttrAttrgroupRelationService;
import daily.boot.gulimall.product.service.AttrGroupService;
import daily.boot.gulimall.product.service.CategoryService;
import daily.boot.gulimall.product.vo.AttrRespVo;
import daily.boot.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.AttrDao;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

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
        if (attrVo.getAttrGroupId() != null) {
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
        queryWrapper.eq(catelogIdValid, AttrEntity::getCatelogId, catelogId);
        String key = pageQueryVo.getKey();
        queryWrapper.and(StringUtils.isNotBlank(key), q ->
                q.eq(AttrEntity::getAttrId, key)
                 .or()
                 .eq(AttrEntity::getAttrName, key)
        );
        IPage<AttrEntity> entityPage = this.page(Query.getPage(pageQueryVo), queryWrapper);
        List<AttrEntity> attrEntities = entityPage.getRecords();
        String queryCatelogName = null;
        if (catelogIdValid) {
            queryCatelogName = getCatelogName(catelogId);
        }
        
        boolean finalCatelogIdValid = catelogIdValid;
        String finalQueryCatelogName = queryCatelogName;
        List<AttrRespVo> respVoList = attrEntities.stream().map(entity -> {
            String attrGroupName = getAttrGroupName(entity.getAttrId());
            String catelogName = finalCatelogIdValid ? finalQueryCatelogName : getCatelogName(entity.getCatelogId());
            AttrRespVo respVo = new AttrRespVo();
            BeanUtils.copyProperties(entity, respVo);
            respVo.setCatelogName(catelogName);
            respVo.setGroupName(attrGroupName);
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
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getByAttrId(attrId);
        if (relationEntity != null) {
            attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
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
}