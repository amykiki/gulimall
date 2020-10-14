package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.OrderSettingDao;
import daily.boot.gulimall.order.entity.OrderSettingEntity;
import daily.boot.gulimall.order.service.OrderSettingService;


@Service("orderSettingService")
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingDao, OrderSettingEntity> implements OrderSettingService {

    @Override
    public PageInfo<OrderSettingEntity> queryPage(PageQueryVo queryVo) {
        IPage<OrderSettingEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}