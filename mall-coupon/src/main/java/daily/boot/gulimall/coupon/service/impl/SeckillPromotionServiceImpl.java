package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillPromotionDao;
import daily.boot.gulimall.coupon.entity.SeckillPromotionEntity;
import daily.boot.gulimall.coupon.service.SeckillPromotionService;


@Service("seckillPromotionService")
public class SeckillPromotionServiceImpl extends ServiceImpl<SeckillPromotionDao, SeckillPromotionEntity> implements SeckillPromotionService {

    @Override
    public PageInfo<SeckillPromotionEntity> queryPage(PageQueryVo queryVo) {
        IPage<SeckillPromotionEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}