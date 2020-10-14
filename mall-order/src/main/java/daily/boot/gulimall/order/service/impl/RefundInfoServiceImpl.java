package daily.boot.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.order.dao.RefundInfoDao;
import daily.boot.gulimall.order.entity.RefundInfoEntity;
import daily.boot.gulimall.order.service.RefundInfoService;


@Service("refundInfoService")
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoDao, RefundInfoEntity> implements RefundInfoService {

    @Override
    public PageInfo<RefundInfoEntity> queryPage(PageQueryVo queryVo) {
        IPage<RefundInfoEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}