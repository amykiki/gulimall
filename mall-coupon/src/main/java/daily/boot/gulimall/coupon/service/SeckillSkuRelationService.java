package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SeckillSkuRelationEntity;

import java.util.List;


/**
 * 秒杀活动商品关联
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @param promotionSessionId
     * @return
     */
    PageInfo<SeckillSkuRelationEntity> queryPage(PageQueryVo queryVo, String promotionSessionId);
    
    List<SeckillSkuRelationEntity> listByPromotionSessionId(Long id);
}

