package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.OrderReturnApplyDao;
import daily.boot.gulimall.order.entity.OrderReturnApplyEntity;
import daily.boot.gulimall.order.service.OrderReturnApplyService;


@Service("orderReturnApplyService")
public class OrderReturnApplyServiceImpl extends ServiceImpl<OrderReturnApplyDao, OrderReturnApplyEntity> implements OrderReturnApplyService {

    @Override
    public PageInfo<OrderReturnApplyEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderReturnApplyEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}