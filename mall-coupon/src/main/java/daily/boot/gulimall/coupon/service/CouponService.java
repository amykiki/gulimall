package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.CouponEntity;


/**
 * 优惠券信息
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
public interface CouponService extends IService<CouponEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<CouponEntity> queryPage(PageQueryVo queryVo);
}

