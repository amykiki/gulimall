package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberStatisticsInfoDao;
import daily.boot.gulimall.member.entity.MemberStatisticsInfoEntity;
import daily.boot.gulimall.member.service.MemberStatisticsInfoService;


@Service("memberStatisticsInfoService")
public class MemberStatisticsInfoServiceImpl extends ServiceImpl<MemberStatisticsInfoDao, MemberStatisticsInfoEntity> implements MemberStatisticsInfoService {

    @Override
    public PageInfo<MemberStatisticsInfoEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberStatisticsInfoEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}