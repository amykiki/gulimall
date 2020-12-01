package daily.boot.gulimall.member.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberLevelDao;
import daily.boot.gulimall.member.entity.MemberLevelEntity;
import daily.boot.gulimall.member.service.MemberLevelService;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageInfo<MemberLevelEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberLevelEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    @Cacheable(value = {"member_level"}, key = "#root.methodName")
    public MemberLevelEntity defaultLevel() {
        return this.lambdaQuery().eq(MemberLevelEntity::getDefaultStatus, 1).one();
    }
}