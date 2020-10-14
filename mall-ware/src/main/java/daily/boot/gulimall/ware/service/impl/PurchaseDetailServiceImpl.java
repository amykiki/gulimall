package daily.boot.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.ware.dao.PurchaseDetailDao;
import daily.boot.gulimall.ware.entity.PurchaseDetailEntity;
import daily.boot.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageInfo<PurchaseDetailEntity> queryPage(PageQueryVo queryVo) {
        IPage<PurchaseDetailEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}