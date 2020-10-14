package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.CouponHistoryDao;
import daily.boot.gulimall.coupon.entity.CouponHistoryEntity;
import daily.boot.gulimall.coupon.service.CouponHistoryService;


@Service("couponHistoryService")
public class CouponHistoryServiceImpl extends ServiceImpl<CouponHistoryDao, CouponHistoryEntity> implements CouponHistoryService {

    @Override
    public PageInfo<CouponHistoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<CouponHistoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}