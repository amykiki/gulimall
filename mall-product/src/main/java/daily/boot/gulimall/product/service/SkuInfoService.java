package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.SkuInfoEntity;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.vo.SpuSaveVo;

import java.util.List;


/**
 * sku信息
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @param skuInfoEntity
     * @return
     */
    PageInfo<SkuInfoEntity> queryPage(PageQueryVo queryVo, SkuInfoEntity skuInfoEntity);
    
    void save(SpuInfoEntity spuInfoEntity, List<SpuSaveVo.Skus> skus);
}

