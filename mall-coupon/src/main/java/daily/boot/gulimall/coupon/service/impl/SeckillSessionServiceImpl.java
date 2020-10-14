package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SeckillSessionDao;
import daily.boot.gulimall.coupon.entity.SeckillSessionEntity;
import daily.boot.gulimall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Override
    public PageInfo<SeckillSessionEntity> queryPage(PageQueryVo queryVo) {
        IPage<SeckillSessionEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}