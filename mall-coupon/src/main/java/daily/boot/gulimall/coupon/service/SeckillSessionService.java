package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SeckillSessionEntity;

import java.util.List;


/**
 * 秒杀活动场次
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SeckillSessionEntity> queryPage(PageQueryVo queryVo);
    
    List<SeckillSessionEntity> getLatest3DaySession();
}

