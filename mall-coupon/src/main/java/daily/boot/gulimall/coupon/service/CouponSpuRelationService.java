package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.CouponSpuRelationEntity;


/**
 * 优惠券与产品关联
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<CouponSpuRelationEntity> queryPage(PageQueryVo queryVo);
}

