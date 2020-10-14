package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillSkuRelationDao;
import daily.boot.gulimall.coupon.entity.SeckillSkuRelationEntity;
import daily.boot.gulimall.coupon.service.SeckillSkuRelationService;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {

    @Override
    public PageInfo<SeckillSkuRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<SeckillSkuRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}