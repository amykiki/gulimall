package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.CouponDao;
import daily.boot.gulimall.coupon.entity.CouponEntity;
import daily.boot.gulimall.coupon.service.CouponService;


@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponDao, CouponEntity> implements CouponService {

    @Override
    public PageInfo<CouponEntity> queryPage(PageQueryVo queryVo) {
        IPage<CouponEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}