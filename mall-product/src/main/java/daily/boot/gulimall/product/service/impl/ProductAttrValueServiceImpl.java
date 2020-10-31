package daily.boot.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import daily.boot.gulimall.product.entity.AttrEntity;
import daily.boot.gulimall.product.service.AttrService;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.ProductAttrValueDao;
import daily.boot.gulimall.product.entity.ProductAttrValueEntity;
import daily.boot.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {
    @Autowired
    private AttrService attrService;
    
    @Override
    public PageInfo<ProductAttrValueEntity> queryPage(PageQueryVo queryVo) {
        IPage<ProductAttrValueEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void save(Long spuId, List<SpuSaveVo.BaseAttrs> baseAttrs) {
        List<Long> attrIds = baseAttrs.stream().map(SpuSaveVo.BaseAttrs::getAttrId).collect(Collectors.toList());
        Map<Long, AttrEntity> attrEntityMap = attrService.listByIds(attrIds).stream()
                                                         .collect(Collectors.toMap(AttrEntity::getAttrId, Function.identity()));
        List<ProductAttrValueEntity> entities = baseAttrs.stream().map(a -> {
            ProductAttrValueEntity entity = new ProductAttrValueEntity();
            entity.setSpuId(spuId);
            entity.setAttrId(a.getAttrId());
            entity.setAttrName(attrEntityMap.get(a.getAttrId()).getAttrName());
            entity.setAttrValue(a.getAttrValues());
            entity.setQuickShow(a.getShowDesc());
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(entities);
    }
    
    @Override
    public List<ProductAttrValueEntity> listBySpuId(Long spuId) {
        LambdaQueryWrapper<ProductAttrValueEntity> queryWrapper =
                Wrappers.lambdaQuery(ProductAttrValueEntity.class)
                        .eq(spuId != null && spuId > 0, ProductAttrValueEntity::getSpuId, spuId);
    
        return this.list(queryWrapper);
    }
    
    @Override
    @Transactional
    public void updateBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities) {
        //由于不知道前端是否对属性有增删，所以先删除之前的属性在同一新增
        this.removeBySpuId(spuId);
        
        //统一新增
        List<ProductAttrValueEntity> list = productAttrValueEntities.stream()
                                                                    .filter(p -> Objects.nonNull(p.getAttrId()) && Objects.nonNull(p.getAttrValue()))
                                                                    .peek(p -> p.setSpuId(spuId))
                                                                    .collect(Collectors.toList());
        this.saveBatch(list);
    
    }
    
    @Override
    public void removeBySpuId(Long spuId) {
        if (Objects.nonNull(spuId)) {
            this.lambdaUpdate()
                .eq(ProductAttrValueEntity::getSpuId, spuId)
                .remove();
        }
    }
}