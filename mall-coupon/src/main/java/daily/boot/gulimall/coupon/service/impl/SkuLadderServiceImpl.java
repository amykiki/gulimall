package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.SkuLadderDao;
import daily.boot.gulimall.coupon.entity.SkuLadderEntity;
import daily.boot.gulimall.coupon.service.SkuLadderService;


@Service("skuLadderService")
public class SkuLadderServiceImpl extends ServiceImpl<SkuLadderDao, SkuLadderEntity> implements SkuLadderService {

    @Override
    public PageInfo<SkuLadderEntity> queryPage(PageQueryVo queryVo) {
        IPage<SkuLadderEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}