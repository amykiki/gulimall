package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberCollectSpuEntity;


/**
 * 会员收藏的商品
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
public interface MemberCollectSpuService extends IService<MemberCollectSpuEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberCollectSpuEntity> queryPage(PageQueryVo queryVo);
}

