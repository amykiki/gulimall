package daily.boot.gulimall.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.utils.Query;
import daily.boot.gulimall.member.dao.MemberReceiveAddressDao;
import daily.boot.gulimall.member.entity.MemberReceiveAddressEntity;
import daily.boot.gulimall.member.service.MemberReceiveAddressService;

import java.util.List;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageInfo<MemberReceiveAddressEntity> queryPage(PageQueryVo queryVo) {
        IPage<MemberReceiveAddressEntity> page = this.page(Query.getPage(queryVo));
        return PageInfo.of(page);
    }
    
    @Override
    public List<MemberReceiveAddressEntity> getAddress(Long memberId) {
        return this.lambdaQuery().eq(MemberReceiveAddressEntity::getMemberId, memberId).list();
    }
}