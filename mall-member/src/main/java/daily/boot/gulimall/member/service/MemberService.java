package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberEntity;


/**
 * 会员
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
public interface MemberService extends IService<MemberEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberEntity> queryPage(PageQueryVo queryVo);
}

