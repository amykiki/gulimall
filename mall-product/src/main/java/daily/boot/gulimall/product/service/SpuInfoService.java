package daily.boot.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import daily.boot.gulimall.common.page.PageQueryVo;
import daily.boot.gulimall.common.page.PageInfo;
import daily.boot.gulimall.product.entity.SpuInfoEntity;
import daily.boot.gulimall.product.vo.SpuSaveVo;


/**
 * spu信息
 *
 * @author amy
 * @date 2020-10-14 15:18:58
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    /**
     * 无条件翻页查询
     * @param queryVo 分页查询参数
     * @param spuInfoEntity
     * @return
     */
    PageInfo<SpuInfoEntity> queryPage(PageQueryVo queryVo, SpuInfoEntity spuInfoEntity);
    
    /**
     * 保存前台传入的spuSaveVo
     * @param spuSaveVo
     */
    void saveSpuInfo(SpuSaveVo spuSaveVo);
}

