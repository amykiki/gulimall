package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.HomeSubjectSpuDao;
import daily.boot.gulimall.coupon.entity.HomeSubjectSpuEntity;
import daily.boot.gulimall.coupon.service.HomeSubjectSpuService;


@Service("homeSubjectSpuService")
public class HomeSubjectSpuServiceImpl extends ServiceImpl<HomeSubjectSpuDao, HomeSubjectSpuEntity> implements HomeSubjectSpuService {

    @Override
    public PageInfo<HomeSubjectSpuEntity> queryPage(PageQueryVo queryVo) {
        IPage<HomeSubjectSpuEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}