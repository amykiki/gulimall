package daily.boot.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.member.entity.MemberLevelEntity;


/**
 * 会员等级
 *
 * @author amy
 * @date 2020-10-14 16:46:51
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<MemberLevelEntity> queryPage(PageQueryVo queryVo);
    
    MemberLevelEntity defaultLevel();
}

