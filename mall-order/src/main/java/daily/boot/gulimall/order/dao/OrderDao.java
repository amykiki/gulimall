package daily.boot.gulimall.order.dao;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

import daily.boot.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author amy
 * @email amy@gmail.com
 * @date 2020-10-14 16:57:10
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
    
    //void update(String orderSn, Integer statusCode, Integer payType);
    int updateStatusAndPayTypeAndPaymentTimeByOrderSn(@Param("updatedStatus")Integer updatedStatus,@Param("updatedPayType")Integer updatedPayType,@Param("updatedPaymentTime")Date updatedPaymentTime,@Param("orderSn")String orderSn);

	
	
}
