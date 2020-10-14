package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SeckillSkuNoticeEntity;


/**
 * 秒杀商品通知订阅
 *
 * @author amy
 * @date 2020-10-14 16:05:21
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SeckillSkuNoticeEntity> queryPage(PageQueryVo queryVo);
}

