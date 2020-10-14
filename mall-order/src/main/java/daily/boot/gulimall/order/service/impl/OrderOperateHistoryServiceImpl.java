package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.OrderOperateHistoryDao;
import daily.boot.gulimall.order.entity.OrderOperateHistoryEntity;
import daily.boot.gulimall.order.service.OrderOperateHistoryService;


@Service("orderOperateHistoryService")
public class OrderOperateHistoryServiceImpl extends ServiceImpl<OrderOperateHistoryDao, OrderOperateHistoryEntity> implements OrderOperateHistoryService {

    @Override
    public PageInfo<OrderOperateHistoryEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderOperateHistoryEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}