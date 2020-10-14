package daily.boot.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.PurchaseDao;
import daily.boot.gulimall.ware.entity.PurchaseEntity;
import daily.boot.gulimall.ware.service.PurchaseService;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageInfo<PurchaseEntity> queryPage(PageQueryVo queryVo) {
        IPage<PurchaseEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}