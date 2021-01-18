package daily.boot.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.WareOrderTaskDao;
import daily.boot.gulimall.ware.entity.WareOrderTaskEntity;
import daily.boot.gulimall.ware.service.WareOrderTaskService;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    @Override
    public PageInfo<WareOrderTaskEntity> queryPage(PageQueryVo queryVo) {
        IPage<WareOrderTaskEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {
        return this.lambdaQuery().eq(WareOrderTaskEntity::getOrderSn, orderSn).one();
    }
}