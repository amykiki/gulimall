package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberCollectSubjectDao;
import daily.boot.gulimall.member.entity.MemberCollectSubjectEntity;
import daily.boot.gulimall.member.service.MemberCollectSubjectService;


@Service("memberCollectSubjectService")
public class MemberCollectSubjectServiceImpl extends ServiceImpl<MemberCollectSubjectDao, MemberCollectSubjectEntity> implements MemberCollectSubjectService {

    @Override
    public PageInfo<MemberCollectSubjectEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberCollectSubjectEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}