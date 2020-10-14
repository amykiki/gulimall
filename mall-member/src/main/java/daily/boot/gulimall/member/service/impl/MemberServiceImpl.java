package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberDao;
import daily.boot.gulimall.member.entity.MemberEntity;
import daily.boot.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageInfo<MemberEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}