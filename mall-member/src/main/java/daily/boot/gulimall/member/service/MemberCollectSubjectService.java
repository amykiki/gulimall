package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberCollectSubjectEntity;


/**
 * 会员收藏的专题活动
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberCollectSubjectEntity> queryPage(PageQueryVo queryVo);
}

