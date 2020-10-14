package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.CouponSpuRelationDao;
import daily.boot.gulimall.coupon.entity.CouponSpuRelationEntity;
import daily.boot.gulimall.coupon.service.CouponSpuRelationService;


@Service("couponSpuRelationService")
public class CouponSpuRelationServiceImpl extends ServiceImpl<CouponSpuRelationDao, CouponSpuRelationEntity> implements CouponSpuRelationService {

    @Override
    public PageInfo<CouponSpuRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<CouponSpuRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}