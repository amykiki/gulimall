package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SkuSaleAttrValueDao;
import daily.boot.gulimall.product.entity.SkuSaleAttrValueEntity;
import daily.boot.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageInfo<SkuSaleAttrValueEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuSaleAttrValueEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}