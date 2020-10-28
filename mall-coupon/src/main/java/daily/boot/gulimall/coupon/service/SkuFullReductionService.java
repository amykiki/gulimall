package daily.boot.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.coupon.entity.SkuFullReductionEntity;
import daily.boot.gulimall.service.api.to.SkuReductionTo;

import java.util.List;


/**
 * 商品满减信息
 *
 * @author amy
 * @date 2020-10-14 16:05:20
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SkuFullReductionEntity> queryPage(PageQueryVo queryVo);
    
    void saveSkuReduction(List<SkuReductionTo> skuReductionTos);
}

