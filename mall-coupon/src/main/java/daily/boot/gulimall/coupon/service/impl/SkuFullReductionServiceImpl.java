package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SkuFullReductionDao;
import daily.boot.gulimall.coupon.entity.SkuFullReductionEntity;
import daily.boot.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Override
    public PageInfo<SkuFullReductionEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuFullReductionEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}