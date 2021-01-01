package daily.boot.gulimall.product.service.impl;

import daily.boot.gulimall.product.vo.SkuItemVo;
import daily.boot.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SkuSaleAttrValueDao;
import daily.boot.gulimall.product.entity.SkuSaleAttrValueEntity;
import daily.boot.gulimall.product.service.SkuSaleAttrValueService;

import java.util.List;
import java.util.stream.Collectors;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageInfo<SkuSaleAttrValueEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuSaleAttrValueEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public void save(Long skuId, List<SpuSaveVo.Skus.SaleAttr> attrs) {
        List<SkuSaleAttrValueEntity> entities = attrs.stream().map(attr -> {
            SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
            BeanUtils.copyProperties(attr, entity);
            entity.setSkuId(skuId);
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(entities);
    }
    
    @Override
    public List<SkuSaleAttrValueEntity> listBySkuIds(List<Long> skuIds) {
        return this.lambdaQuery().in(SkuSaleAttrValueEntity::getSkuId, skuIds).list();
    }
    
    @Override
    public List<SkuItemVo.SkuItemSaleAttr> listSaleAttrBySpuId(Long spuId) {
        return this.baseMapper.listSaleAttrBySpuId(spuId);
    }
    
    @Override
    public List<String> getSkuSaleAttrValuesAsString(Long skuId) {
        return this.baseMapper.getSkuSaleAttrValuesAsStringList(skuId);
    }
}