package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.SkuSaleAttrValueEntity;


/**
 * sku销售属性&值
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SkuSaleAttrValueEntity> queryPage(PageQueryVo queryVo);
}

