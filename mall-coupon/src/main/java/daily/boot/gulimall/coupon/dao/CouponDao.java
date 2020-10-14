package daily.boot.gulimall.coupon.dao;

import daily.boot.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 16:05:20
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
