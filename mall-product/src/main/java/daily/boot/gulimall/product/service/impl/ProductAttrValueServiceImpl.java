package daily.boot.gulimall.product.service.impl;

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

import java.util.List;
import java.util.Map;
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
}