package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.CouponSpuCategoryRelationDao;
import daily.boot.gulimall.coupon.entity.CouponSpuCategoryRelationEntity;
import daily.boot.gulimall.coupon.service.CouponSpuCategoryRelationService;


@Service("couponSpuCategoryRelationService")
public class CouponSpuCategoryRelationServiceImpl extends ServiceImpl<CouponSpuCategoryRelationDao, CouponSpuCategoryRelationEntity> implements CouponSpuCategoryRelationService {

    @Override
    public PageInfo<CouponSpuCategoryRelationEntity> queryPage(PageQueryVo queryVo) {
        IPage<CouponSpuCategoryRelationEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}