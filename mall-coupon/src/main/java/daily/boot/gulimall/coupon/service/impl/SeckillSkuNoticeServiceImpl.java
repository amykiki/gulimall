package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillSkuNoticeDao;
import daily.boot.gulimall.coupon.entity.SeckillSkuNoticeEntity;
import daily.boot.gulimall.coupon.service.SeckillSkuNoticeService;


@Service("seckillSkuNoticeService")
public class SeckillSkuNoticeServiceImpl extends ServiceImpl<SeckillSkuNoticeDao, SeckillSkuNoticeEntity> implements SeckillSkuNoticeService {

    @Override
    public PageInfo<SeckillSkuNoticeEntity> queryPage(PageQueryVo queryVo) {
        IPage<SeckillSkuNoticeEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}