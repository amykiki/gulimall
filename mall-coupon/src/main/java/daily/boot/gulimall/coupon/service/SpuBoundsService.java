package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SpuBoundsEntity;


/**
 * 商品spu积分设置
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SpuBoundsEntity> queryPage(PageQueryVo queryVo);
}

