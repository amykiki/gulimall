package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.HomeAdvEntity;


/**
 * 首页轮播广告
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<HomeAdvEntity> queryPage(PageQueryVo queryVo);
}

