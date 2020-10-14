package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.PaymentInfoDao;
import daily.boot.gulimall.order.entity.PaymentInfoEntity;
import daily.boot.gulimall.order.service.PaymentInfoService;


@Service("paymentInfoService")
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoDao, PaymentInfoEntity> implements PaymentInfoService {

    @Override
    public PageInfo<PaymentInfoEntity> queryPage(PageQueryVo queryVo) {
        IPage<PaymentInfoEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}