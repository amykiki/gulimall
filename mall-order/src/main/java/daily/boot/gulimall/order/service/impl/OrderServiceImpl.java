package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.OrderDao;
import daily.boot.gulimall.order.entity.OrderEntity;
import daily.boot.gulimall.order.service.OrderService;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Override
    public PageInfo<OrderEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}