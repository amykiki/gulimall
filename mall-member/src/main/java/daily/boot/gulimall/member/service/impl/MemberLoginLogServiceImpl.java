package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberLoginLogDao;
import daily.boot.gulimall.member.entity.MemberLoginLogEntity;
import daily.boot.gulimall.member.service.MemberLoginLogService;


@Service("memberLoginLogService")
public class MemberLoginLogServiceImpl extends ServiceImpl<MemberLoginLogDao, MemberLoginLogEntity> implements MemberLoginLogService {

    @Override
    public PageInfo<MemberLoginLogEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberLoginLogEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}