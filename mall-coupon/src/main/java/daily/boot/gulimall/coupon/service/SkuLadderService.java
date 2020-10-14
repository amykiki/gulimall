package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SkuLadderEntity;


/**
 * 商品阶梯价格
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SkuLadderEntity> queryPage(PageQueryVo queryVo);
}

