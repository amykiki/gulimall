package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberCollectSpuDao;
import daily.boot.gulimall.member.entity.MemberCollectSpuEntity;
import daily.boot.gulimall.member.service.MemberCollectSpuService;


@Service("memberCollectSpuService")
public class MemberCollectSpuServiceImpl extends ServiceImpl<MemberCollectSpuDao, MemberCollectSpuEntity> implements MemberCollectSpuService {

    @Override
    public PageInfo<MemberCollectSpuEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberCollectSpuEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }

}