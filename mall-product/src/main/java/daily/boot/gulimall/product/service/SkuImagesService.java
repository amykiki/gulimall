package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.SkuImagesEntity;
import daily.boot.gulimall.product.vo.SpuSaveVo;

import java.util.List;


/**
 * sku图片
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @return
     */
    PageInfo<SkuImagesEntity> queryPage(PageQueryVo queryVo);
    
    void saveSku(Long skuId, List<SpuSaveVo.Skus.Image> images);
}

