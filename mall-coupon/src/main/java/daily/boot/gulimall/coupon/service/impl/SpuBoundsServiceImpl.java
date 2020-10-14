package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SpuBoundsDao;
import daily.boot.gulimall.coupon.entity.SpuBoundsEntity;
import daily.boot.gulimall.coupon.service.SpuBoundsService;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageInfo<SpuBoundsEntity> queryPage(PageQueryVo queryVo) {
        IPage<SpuBoundsEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}