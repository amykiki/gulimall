package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberReceiveAddressEntity;

import java.util.List;


/**
 * 会员收货地址
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberReceiveAddressEntity> queryPage(PageQueryVo queryVo);
    
    List<MemberReceiveAddressEntity> getAddress(Long memberId);
}

