package daily.boot.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.product.dao.SkuInfoDao;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageInfo<SkuInfoEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuInfoEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}