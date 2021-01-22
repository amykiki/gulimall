package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.OrderItemDao;
import daily.boot.gulimall.order.entity.OrderItemEntity;
import daily.boot.gulimall.order.service.OrderItemService;

import java.util.List;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageInfo<OrderItemEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderItemEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<OrderItemEntity> listByOrderSn(String orderSn) {
        return this.lambdaQuery().eq(OrderItemEntity::getOrderSn, orderSn).list();
    }
}