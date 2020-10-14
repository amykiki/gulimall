package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.HomeAdvDao;
import daily.boot.gulimall.coupon.entity.HomeAdvEntity;
import daily.boot.gulimall.coupon.service.HomeAdvService;


@Service("homeAdvService")
public class HomeAdvServiceImpl extends ServiceImpl<HomeAdvDao, HomeAdvEntity> implements HomeAdvService {

    @Override
    public PageInfo<HomeAdvEntity> queryPage(PageQueryVo queryVo) {
        IPage<HomeAdvEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}