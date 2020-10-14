package daily.boot.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.coupon.dao.HomeSubjectDao;
import daily.boot.gulimall.coupon.entity.HomeSubjectEntity;
import daily.boot.gulimall.coupon.service.HomeSubjectService;


@Service("homeSubjectService")
public class HomeSubjectServiceImpl extends ServiceImpl<HomeSubjectDao, HomeSubjectEntity> implements HomeSubjectService {

    @Override
    public PageInfo<HomeSubjectEntity> queryPage(PageQueryVo queryVo) {
        IPage<HomeSubjectEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}